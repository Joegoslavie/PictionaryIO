module pictionaryserver {
    requires pictionaryshared;

    requires slf4j.api;
    requires gson;
    requires java.sql;

    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.servlet;
    requires org.eclipse.jetty.websocket.javax.websocket.server;

    requires javax.servlet.api;
    requires javax.websocket.api;

    exports main;
}