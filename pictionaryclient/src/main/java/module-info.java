module pictionary {
    requires javafx.fxml;
    requires javafx.controls;
    requires pictionaryshared;
    requires javax.websocket.api;
    requires gson;
    requires slf4j.api;
    requires java.sql;
    requires org.eclipse.jetty.util;
    requires org.junit.jupiter.api;

    opens main;
    exports main;
    exports controllers;
    exports logicModels;
}