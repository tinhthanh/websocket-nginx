package com.pal.websocketnginx.controller.ws.conn;

import com.pal.websocketnginx.controller.ws.common.MapBaseCache;
import org.springframework.stereotype.Repository;

@Repository
public class WSSessionCache extends MapBaseCache<String, WSSession> {
}
