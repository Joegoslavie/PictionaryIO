package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import logicModels.Connection;
import util.Screen;
import util.ScreenLoader;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ScreenLoader.getInstance().loadScreen(Screen.LOGIN);
    }

    @Override
    public void stop() {
        Connection.getInstance().stopClient();
        Platform.exit();
    }
}
