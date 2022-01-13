package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.servlet.ServletException;
import javax.websocket.server.ServerContainer;

public class Main {
    private static final int PORT = 8095;

    public static void main(String[] args) {
        Server webSocketServer = new Server();
        ServerConnector connector = new ServerConnector(webSocketServer);
        connector.setPort(PORT);
        webSocketServer.addConnector(connector);

        ServletContextHandler webSocketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        webSocketContext.setContextPath("/");
        webSocketServer.setHandler(webSocketContext);

        try {
            ServerContainer container = WebSocketServerContainerInitializer.configureContext(webSocketContext);
            //ServerContainer container = (ServerContainer)webSocketContext.getAttribute("javax.websocket.server.ServerContainer");
            container.addEndpoint(Connection.class);

            webSocketServer.start();
            webSocketServer.join();
        } catch(InterruptedException | ServletException exc){
            System.out.println(exc);
        } catch(Exception exc){
            System.out.println(exc);
        }
    }
}