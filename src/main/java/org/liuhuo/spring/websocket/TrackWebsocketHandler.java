package org.liuhuo.spring.websocket;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.liuhuo.spring.model.RemoteData;
import org.liuhuo.spring.dao.RemoteDataDao;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class TrackWebsocketHandler extends TextWebSocketHandler {
    @Autowired
    private RemoteDataDao remoteDataDao;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage text) throws Exception {
        int value = 0;
        int count = 0;
        while (true) {
            Thread.sleep(1010);
            List<RemoteData> data = remoteDataDao.findRecordInOneSec();
            if (data == null || data.size() == 0) continue;
            System.out.println(data.size());
            ObjectMapper mapper = new ObjectMapper();
            //System.out.println(mapper.writeValueAsString(data));
            // StringBuilder sb = new StringBuilder();
            // sb.append("<tr>");
            // sb.append("<td>");
            // sb.append(value++);
            // sb.append("</td>");
            // sb.append("<td>");
            // sb.append(value++);
            // sb.append("</td>");
            // sb.append("<td>");
            // sb.append(value++);
            // sb.append("</td>");
            // sb.append("</tr>");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(data)));
            count++;
            if (count == 10) break;
        }
    }
}
