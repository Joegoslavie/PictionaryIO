package controllers;

import com.google.gson.Gson;
import enums.Header;
import enums.MessageType;
import interfacesControllers.IDrawerCanvasController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logicModels.Connection;
import models.*;
import util.ScoreComparator;
import util.Screen;
import util.ScreenLoader;
import webSocketMessages.WebSocketMessage;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerCanvasController implements Initializable, IDrawerCanvasController {

    public TextField txtDrawer;
    public TextField txtTimeLeft;
    public ListView lbChat;
    public TextField tbChatMessage;
    public TextField txtSelectedWord;
    public Button btnLeave;
    public Canvas canvasMain;
    public ColorPicker cpDrawingColor;
    public Slider sldLineWidth;
    public Button btnClear;
    public ListView lvLeaderboard;
    public Button btnWord1;
    public Button btnWord2;
    public Button btnWord3;

    public ObservableList<LeaderboardEntry> leaderboardEntries;
    public int timerValue;
    public Game game;

    private boolean canDraw;
    private ArrayList<String> words;
    private double lastX;
    private double lastY;
    private Color color;
    private double lineWidth;
    private Gson gson;

    private boolean showAlerts;

    private static final int MAX_CHAT_SIZE = 30;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GraphicsContext gc = canvasMain.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        lineWidth = sldLineWidth.getValue();
        cpDrawingColor.setValue(Color.BLACK);
        color = Color.BLACK;

        canvasMain.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });

        canvasMain.setOnMouseDragged(e -> {
            double x = e.getX();
            double y = e.getY();

            drawLine(lastX, lastY, x, y, this.color, this.lineWidth, false);

            sendPoint(lastX, lastY, x, y);

            lastX = x;
            lastY = y;

        });

        gson = new Gson();
        Connection.getInstance().setDrawerController(this);
    }

    public void setGame(Game game) {
        this.game = game;
        updateGameInfo();
    }

    public void updateGameInfo() {
        Platform.runLater(() -> {
            leaderboardEntries = FXCollections.observableArrayList();

            for(LeaderboardEntry leaderboardEntry : game.getLeaderboardEntries()) {
                leaderboardEntries.add(leaderboardEntry);
            }

            lvLeaderboard.setItems(leaderboardEntries);

            List<Round> rounds = game.getRounds();
            Round currentRound = rounds.get(rounds.size() - 1);
            String drawerName = currentRound.getDrawerName();

            Player drawerPlayer = null;

            for(Player player : game.getPlayers()) {
                if(player.getUsername().equals(drawerName)) {
                    drawerPlayer = player;
                    break;
                }
            }

            if(drawerPlayer != null) {
                txtDrawer.setText(drawerName);
                //lblYourUsername.setText(Connection.getInstance().getLocalPlayer().getUsername());
            }
        });
    }

    private void sendPoint(double lastX, double lastY, double x, double y) {
        if (canDraw) {
            LinePoints points = new LinePoints(x,y, lastX, lastY, toRGBCode(color), lineWidth);
            WebSocketMessage message = new WebSocketMessage(Header.POINT, gson.toJson(points));
            Connection.getInstance().sendMessage(message);
        }
    }

    private void drawLine(double lastX, double lastY, double xPos, double yPos, Color color, double lineWidth, boolean isDrawRemote) {
        if (canDraw || isDrawRemote) {
            GraphicsContext gc = canvasMain.getGraphicsContext2D();;

            if(color == null) {
                color = this.color;
            }

            gc.setLineWidth(lineWidth);
            gc.setStroke(color);
            gc.strokeLine(lastX, lastY, xPos, yPos);
        }
    }


    @Override
    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    @Override
    public void drawRemotePoint(double lastX, double lastY, double x, double y, String color, double lineWidth) {
        Platform.runLater(() -> drawLine(lastX, lastY, x, y, Color.web(color), lineWidth, true));
    }

    @Override
    public void clear() {
        clearCanvas(null);
    }

    public void onColorChange(ActionEvent actionEvent) {
        color = cpDrawingColor.getValue();
    }

    public void onLineWidthChange(MouseEvent mouseEvent) {
        lineWidth = sldLineWidth.getValue();
    }

    public void clearCanvas(ActionEvent actionEvent) {
        if(actionEvent != null) {
            //Button was pressed
            String DrawerName = game.getCurrentRound().getDrawerName();
            String localPlayerName = Connection.getInstance().getLocalPlayer().getUsername();
            if(DrawerName.equals(localPlayerName)) {
                Connection.getInstance().sendMessage(new WebSocketMessage(Header.CLEARCANVAS, ""));
            }
        } else {
            GraphicsContext gc = canvasMain.getGraphicsContext2D();
            gc.clearRect(0.0, 0.0, canvasMain.getWidth(), canvasMain.getHeight());
        }
    }

    public void onEnterPressed(ActionEvent actionEvent) {
        if(tbChatMessage.getText().equals("")) {
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        Connection connection = Connection.getInstance();
        Player localPlayer = connection.getLocalPlayer();
        String localUsername = localPlayer.getUsername();
        int roundCounter = localPlayer.getTurnCount();

        Message message = new Message(new Date(dateFormat.format(date)), roundCounter, tbChatMessage.getText(), localUsername, MessageType.DEFAULT);

        tbChatMessage.setText("");

        WebSocketMessage socketMessage = new WebSocketMessage(Header.CHATMESSAGE, gson.toJson(message));
        Connection.getInstance().sendMessage(socketMessage);
    }

    @Override
    public void showChatMessage(Message message) {
        Platform.runLater(() -> {
            lbChat.getItems().add(message);
            lbChat.scrollTo(lbChat.getItems().size() - 1);
        });
    }

    private String toRGBCode(Color color)
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    @Override
    public void showWords(List<String> newWords) {
        Platform.runLater(() -> {
            btnWord1.setText(newWords.get(0));
            btnWord2.setText(newWords.get(1));
            btnWord3.setText(newWords.get(2));

            btnWord1.setVisible(true);
            btnWord2.setVisible(true);
            btnWord3.setVisible(true);
        });

        words = (ArrayList<String>)newWords;
    }

    @Override
    public void updateRound(Round round) {

    }

    @Override
    public void roundStart(int time) {

        timerValue = time;
        Timer timer = new Timer();
        if(time == -1) {
            Platform.runLater(() -> {
                txtTimeLeft.setText("<Waiting>");
                txtSelectedWord.setText("");
                Text text = new Text();
                text.setText("New round is starting!");
                text.setFill(Color.BLUE);
                lbChat.getItems().add(text);
            });
        } else {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (timerValue > 0) {
                        Platform.runLater(() -> txtTimeLeft.setText(Integer.toString(timerValue)));
                        timerValue--;
                    } else {
                        timer.cancel();
                    }
                }
            }, 0, 1000);
        }
    }

    @Override
    public void guessedWord(LeaderboardEntry leaderboardEntry) {
        Platform.runLater(() -> {
            Text text = new Text();
            text.setText(leaderboardEntry.getUsername() + " has guessed the word!");
            text.setFill(Color.GREEN);
            lbChat.getItems().add(text);
        });
    }

    @Override
    public void updateLeaderboard(LeaderboardEntry leaderboardEntry) {
        Platform.runLater(() -> {
            for (LeaderboardEntry l : leaderboardEntries) {
                if (l.getUsername().equals(leaderboardEntry.getUsername())) {
                    l.setScore(leaderboardEntry.getScore());
                }
            }

            Collections.sort(leaderboardEntries, new ScoreComparator());

            lvLeaderboard.refresh();
        });
    }

    @Override
    public void playerLeftEvent(Player player) {
        this.game.getPlayers().remove(player);
        Platform.runLater(() -> {
            Text text = new Text();
            text.setText(player.getUsername() + " has left the game");
            text.setFill(Color.RED);
            lbChat.getItems().add(text);

            // Untested code incoming!!!
            ArrayList<LeaderboardEntry> tempEntries = new ArrayList<>();

            for(LeaderboardEntry entry : leaderboardEntries){
                if(entry.getUsername().toLowerCase().equals(player.getUsername().toLowerCase())){
                    tempEntries.add(entry);
                }
            }

            leaderboardEntries.removeAll(tempEntries);
            lvLeaderboard.refresh();

            if(Connection.getInstance().getLocalPlayer().getUsername().toLowerCase().equals(player.getUsername().toLowerCase())){

                Stage gameStage = (Stage) btnLeave.getScene().getWindow();
                ScreenLoader.getInstance().loadScreen(Screen.LOBBYOVERVIEW);
                gameStage.close();
            }
        });
    }

    @Override
    public void updateCurrentDrawerName(String currentDrawer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtDrawer.setText(currentDrawer);
            }
        });
    }

    public void word1Click(ActionEvent actionEvent) {
        setWordButtonsVisable(false);
        Connection.getInstance().sendPickWord(words.get(0));
        txtSelectedWord.setText(words.get(0));
    }

    public void word2Click(ActionEvent actionEvent) {
        setWordButtonsVisable(false);
        Connection.getInstance().sendPickWord(words.get(1));
        txtSelectedWord.setText(words.get(1));
    }

    public void word3Click(ActionEvent actionEvent) {
        setWordButtonsVisable(false);
        Connection.getInstance().sendPickWord(words.get(2));
        txtSelectedWord.setText(words.get(2));
    }

    public void setWordButtonsVisable(boolean isVisable) {
        btnWord1.setVisible(isVisable);
        btnWord2.setVisible(isVisable);
        btnWord3.setVisible(isVisable);
    }

    public void btnLeaveClick(ActionEvent actionEvent) {
        Connection.getInstance().sendMessage(new WebSocketMessage(Header.GAMELEAVE, null));
    }

    @Override
    public void updateEndGame() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage gameStage = (Stage)btnClear.getScene().getWindow();
                ScreenLoader.getInstance().loadScreen(Screen.ENDGAME, game.getLeaderboardEntries(), gameStage);
                gameStage.close();
            }
        });
    }

    @Override
    public void showRoundWord(String roundWord) {
        Platform.runLater(() -> {
            Text text = new Text();
            text.setText("The word was: " + roundWord);
            text.setFill(Color.GREEN);
            lbChat.getItems().add(text);
        });
    }

    @Override
    public void showGuessAlmostCorrect(String guessedWord) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Text text = new Text();
                text.setText(guessedWord + " was almost correct!");
                text.setFill(Color.ORANGE);
                lbChat.getItems().add(text);
            }
        });
    }

    public void onKeyTyped(KeyEvent keyEvent) {
        int chatTextBoxLength = tbChatMessage.getLength();
        if(chatTextBoxLength >= MAX_CHAT_SIZE) {
            String oldText = tbChatMessage.getText();
            String newText = oldText.substring(0, oldText.length() - 1);
            tbChatMessage.setText(newText);
            tbChatMessage.positionCaret(newText.length());
        }
    }
}
