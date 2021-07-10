package com.pal.websocketnginx.controller.ws.conn;

import com.pal.websocketnginx.controller.ws.common.WSConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import javax.annotation.PreDestroy;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@EnableWebSocketMessageBroker
public class WSConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = Logger.getLogger(WSConfig.class.getName());

    @Autowired
    private WSSessionCache webSocketSessionCache;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/mews")
                .setAllowedOrigins("*")
                .addInterceptors(new CustomHandshakeInterceptor());
    }



    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(org.springframework.web.socket.WebSocketSession session) throws Exception {
                        super.afterConnectionEstablished(session);
                        String sessionId = session.getId();
                        WSSession oddsSession = webSocketSessionCache.get(sessionId);
                        if (oddsSession == null) {
                            webSocketSessionCache.put(sessionId, new WSSession(session));
                        }
                        logger.log(Level.INFO, "WebSocketConfig > afterConnectionEstablished > ClientIp={0}, SessionId={1}", new Object[]{session.getAttributes().get(WSConstant.CLIENT_IP), session.getId()});
                    }
                };
            }
        });
        registration.setMessageSizeLimit(512 * 1024);
        registration.setSendBufferSizeLimit(2 * 1024 * 1024);
    }

    @PreDestroy
    public void stop() {
        for (WSSession wsSession : webSocketSessionCache.values()) {
            wsSession.close();
        }
        webSocketSessionCache.invalidateAll();
        logger.log(Level.INFO, "{0} stopped", WSConfig.class.getSimpleName());
    }
}
