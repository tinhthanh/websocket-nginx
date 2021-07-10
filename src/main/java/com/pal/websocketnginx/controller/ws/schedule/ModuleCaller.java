package com.pal.websocketnginx.controller.ws.schedule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.LoadingCache;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ModuleCaller {

    private static final Logger logger = Logger.getLogger(ModuleCaller.class.getName());
    private static final List<Header> DEFAULT_HEADERS = getDefaultHeaders();
    private static final int TIME_OUT = 2500;
    private LoadingCache<String, List<String>> allUrlsCache;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(50);

    @Value("${env.is.blue:false}")
    private boolean blue;

    @PostConstruct
    void init() {
         }


    public void broadcast(String module, String path, Object body) {
        getUrls(module, path).forEach(url -> {
            this.scheduledExecutorService.submit(() ->{
                try {
                    sendRequest(url, body, DEFAULT_HEADERS);
                } catch (Exception ex) {
                    logger.log(Level.INFO, "Cannot broadcast to {0} with body {1} exception {2}",
                            new Object[]{url, Optional.ofNullable(body).map(Object::toString).orElse(""), ex});
                }
            });
        });
    }

    public String sendRequest(String url, Object body, List<Header> headers) {
        String params = "";
        try (CloseableHttpClient client = createClient()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            params = body instanceof String ? (String) body : objectMapper.writeValueAsString(body);
            HttpPost request = new HttpPost(url);
            request.setEntity(new ByteArrayEntity(params.getBytes(StandardCharsets.UTF_8)));
            request.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.forEach(request::setHeader);
            CloseableHttpResponse resp = client.execute(request);
            String response = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
            if (HttpStatus.SC_OK != resp.getStatusLine().getStatusCode()) {
                logger.log(Level.WARNING, "Cannot send request to " + url + " with params: " + params + " response code " + resp.getStatusLine().getStatusCode() + " response: " + response);
                throw new RuntimeException("System error");
            }
            logger.log(Level.FINEST, "Send request to " + url + " with data " + params + " and response: " + response);

            return response;
        } catch (IOException | RuntimeException ex) {
            logger.log(Level.WARNING, "Cannot send request to " + url + " with params: " + params, ex);
            throw new RuntimeException(ex);
        }
    }

    private List<String> getUrls(String module, String path) {
        try {
            return allUrlsCache.get(module)
                    .stream().map(url -> url + (path.startsWith("/") ? "" : "/") + path).collect(Collectors.toList());
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }

    private CloseableHttpClient createClient() {
        try {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(TIME_OUT)
                    .setSocketTimeout(TIME_OUT)
                    .setConnectionRequestTimeout(TIME_OUT)
                    .build();
            SSLConnectionSocketFactory sslFactory = createTrustAllFactory();
            CloseableHttpClient client = HttpClientBuilder.create().setDefaultHeaders(DEFAULT_HEADERS)
                    .setDefaultRequestConfig(config)
                    .setSSLSocketFactory(sslFactory)
                    .setConnectionManager(new PoolingHttpClientConnectionManager(createTrustAllFactoryRegistry(sslFactory))).build();
            HttpComponentsClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory(client);
            clientFactory.setConnectTimeout(TIME_OUT);
            clientFactory.setReadTimeout(TIME_OUT);
            clientFactory.setConnectionRequestTimeout(TIME_OUT);
            return client;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
            logger.log(Level.WARNING, "Cannot init client", ex);
            throw new RuntimeException(ex);
        }
    }

    private Registry<ConnectionSocketFactory> createTrustAllFactoryRegistry(SSLConnectionSocketFactory sslFactory) {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslFactory).build();
    }

    private SSLConnectionSocketFactory createTrustAllFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return new SSLConnectionSocketFactory(SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build(), (host, ssl) -> true);
    }

    private static List<Header> getDefaultHeaders() {
        return Arrays.asList(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age=0"),
                new BasicHeader(HttpHeaders.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.3"),
                new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "es-ES,es;q=0.8"),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
                new BasicHeader(HttpHeaders.ACCEPT, "application/json"),
                new BasicHeader(HttpHeaders.PRAGMA, ""));
    }
}
