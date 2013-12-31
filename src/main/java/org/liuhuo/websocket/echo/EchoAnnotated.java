package org.liuhuo.websocket.echo;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.OnMessage;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/echoAnnotation")
public class EchoAnnotated {

    @OnMessage
    public void echoTextMessage(Session session, String msg, boolean last) throws Exception {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(msg+" "+msg, last);
                int count = 0;
                while (true) {
                    Thread.sleep(1000);
                    session.getBasicRemote().sendText(msg+" "+count, last);
                    count++;
                }
            }
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }

    @OnMessage
    public void echoBinaryMessage(Session session, ByteBuffer bb,
            boolean last) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendBinary(bb, last);
            }
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }

    /**
     * Process a received pong. This is a NO-OP.
     *
     * @param pm    Ignored.
     */
    @OnMessage
    public void echoPongMessage(PongMessage pm) {
        // NO-OP
    }
}
