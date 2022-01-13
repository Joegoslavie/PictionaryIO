module pictionaryshared {
    opens restmodels;

    requires gson;
    requires javax.websocket.api;
    requires javafx.graphics;

    opens webSocketMessages;
    opens models;

    exports enums;
    exports interfaces;
    exports models;
    exports webSocketMessages;
    exports restmodels;
    exports factories;
    exports observerpattern;
}