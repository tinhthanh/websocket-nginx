package com.pal.websocketnginx.controller.ws.conn;

import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WSSession {

    private static final Logger logger = Logger.getLogger(WSSession.class.getName());

    private WebSocketSession session;

    private String sessionId;

    private String servletSessionId;

    private String clientIp;

    private long lastSendTimestamp;

    private Set<String> destinations = ConcurrentHashMap.newKeySet();

    public WSSession(WebSocketSession session) {
        sessionId = session.getId();
        this.session = session;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getServletSessionId() {
        return servletSessionId;
    }

    public void setServletSessionId(String servletSessionId) {
        this.servletSessionId = servletSessionId;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public Set<String> getDestinations() {
        return destinations;
    }

    public void addDestination(String destination) {
        destinations.add(destination);
    }

    public void setDestinations(Set<String> destinations) {
        this.destinations = destinations;
    }

    public void removeDestination(String destination) {
        this.destinations.remove(destination);
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientIp() {
        return clientIp;
    }

    public long getLastSendTimestamp() {
        return lastSendTimestamp;
    }

    public void setLastSendTimestamp(long lastSendTimestamp) {
        this.lastSendTimestamp = lastSendTimestamp;
    }

    public void close() {
        try {
            session.close();
        } catch (Throwable ex) {
            logger.log(Level.INFO, "");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WSSession that = (WSSession) o;
        return Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WSSession{");
        sb.append("session=").append(session);
        sb.append(", sessionId='").append(sessionId).append('\'');
        sb.append(", servletSessionId='").append(servletSessionId).append('\'');
        sb.append(", clientIp='").append(clientIp).append('\'');
        sb.append(", destinations=").append(destinations);
        sb.append(", lastSendTimestamp=").append(lastSendTimestamp);
        sb.append('}');
        return sb.toString();
    }
}
