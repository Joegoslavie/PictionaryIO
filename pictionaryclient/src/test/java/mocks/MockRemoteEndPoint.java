package mocks;

import javax.websocket.RemoteEndpoint;
import javax.websocket.SendHandler;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public class MockRemoteEndPoint implements RemoteEndpoint.Async {
    private boolean sent;

    public boolean getSent(){
        return this.sent;
    }

    @Override
    public long getSendTimeout() {
        return 0;
    }

    @Override
    public void setSendTimeout(long l) {

    }

    @Override
    public void sendText(String s, SendHandler sendHandler) {
        sent = true;
    }

    @Override
    public Future<Void> sendText(String s) {
        sent = true;
        return null;
    }

    @Override
    public Future<Void> sendBinary(ByteBuffer byteBuffer) {
        return null;
    }

    @Override
    public void sendBinary(ByteBuffer byteBuffer, SendHandler sendHandler) {

    }

    @Override
    public Future<Void> sendObject(Object o) {
        return null;
    }

    @Override
    public void sendObject(Object o, SendHandler sendHandler) {

    }

    @Override
    public void setBatchingAllowed(boolean b) throws IOException {

    }

    @Override
    public boolean getBatchingAllowed() {
        return false;
    }

    @Override
    public void flushBatch() throws IOException {

    }

    @Override
    public void sendPing(ByteBuffer byteBuffer) throws IOException, IllegalArgumentException {

    }

    @Override
    public void sendPong(ByteBuffer byteBuffer) throws IOException, IllegalArgumentException {

    }
}
