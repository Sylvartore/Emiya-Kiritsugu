//
// Created by Sylvartore on 3/8/2019.
//
package sylvartore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class UI extends Application {

    private VBox root;
    private GridPane board;
    Square[][] squares;
    Game game;
    int time;
    Label timeLabel;
    boolean pause;
    Label bs;
    Label ws;
    int humanTime;
    boolean humanTurn;
    int mode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Abalone");
        pause = true;
        humanTurn = true;
        time = 0;
        mode = 0;
        humanTime = 60;
        root = new VBox();
        board = new GridPane();
        squares = new Square[10][18];
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j] = new Square(-1, i, j);
                board.add(squares[i][j], j, i);
            }
        }

        game = new Game();
        game.init(this);
        game.update();
        root.getChildren().add(board);
        add_btn();
        Scene mainScene = new Scene(root, 800, 1000);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    void add_btn() {
        HBox btnRow = new HBox();
        add_standard(btnRow);
        add_bel(btnRow);
        add_ger(btnRow);
        add_start(btnRow);
        add_ava(btnRow);
        root.getChildren().add(btnRow);
        HBox textRow = new HBox();
        add_time_limit(textRow);
        add_turn_limit(textRow);
        root.getChildren().add(textRow);
        add_scores();

        add_timer();
        add_logger();
    }

    void add_logger() {
        HBox loggerRow = new HBox();
        Button console_log = new Button();
        console_log.setText("Log to Console");
        console_log.setPrefWidth(120);
        console_log.setOnMouseClicked(event -> game.outputLog(true));
        loggerRow.getChildren().add(console_log);

        Button file_log = new Button();
        file_log.setText("Log to File");
        file_log.setPrefWidth(120);
        file_log.setOnMouseClicked(event -> game.outputLog(false));
        loggerRow.getChildren().add(file_log);

//        switch_qui(loggerRow);
//        switch_main(loggerRow);
        root.getChildren().add(loggerRow);
    }

    void add_timer() {
        HBox timerRow = new HBox();

        TextField timeLimit = new TextField();
        timeLimit.setText("60");
        timerRow.getChildren().add(timeLimit);

        Button setTimeLimit = new Button();
        setTimeLimit.setText("Set Human time(s)");
        setTimeLimit.setPrefWidth(120);
        setTimeLimit.setOnMouseClicked(event -> {
            int time = Integer.parseInt(timeLimit.getText());
            humanTime = time;
            resetTime();
            timeLabel.setText(getTime());
        });
        timerRow.getChildren().add(setTimeLimit);

        Label timerLabel = new Label();
        timerLabel.setText("        Time used:      ");
        timerRow.getChildren().add(timerLabel);

        timeLabel = new Label();
        timeLabel.setText("60");
        timeLabel.setPrefWidth(50);
        timerRow.getChildren().add(timeLabel);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (pause) return;
                Platform.runLater(() -> timeLabel.setText(getTime()));
                counterDown();
            }
        }, 0, 1000);
        add_pause(timerRow);
        add_undo(timerRow);
        add_reset(timerRow);
        root.getChildren().add(timerRow);
    }

    void add_ava(HBox btnRow) {
        Button AivAi = new Button();
        AivAi.setText("AI vs AI");
        AivAi.setPrefWidth(120);
        AivAi.setOnMouseClicked(event -> {
            System.out.println("AI vs AI start: ");
            humanTurn = false;
            game.humanSide = 0;
            if (game.ai.side == -1) {
                mainAiMove();
            } else {
                counterAiMove();
            }
        });
        btnRow.getChildren().add(AivAi);
    }

    void mainAiMove() {
        (new Thread(() -> {
            game.aiMove(game.ai);
            Platform.runLater(() -> {
                game.update();
                if (game.turnLeft == 0 || game.gameOver) return;
                counterAiMove();
            });
        })).start();
    }

    void counterAiMove() {
        (new Thread(() -> {
            game.aiMove(game.counter);
            Platform.runLater(() -> {
                game.update();
                if (game.turnLeft == 0 || game.gameOver) return;
                mainAiMove();
            });
        })).start();
    }

    void add_scores() {
        HBox score = new HBox();
        bs = new Label();
        bs.setText("Black Score: 0      ");
        bs.setPrefWidth(200);
        score.getChildren().add(bs);
        ws = new Label();
        ws.setText("White Score: 0      ");
        score.getChildren().add(ws);
        root.getChildren().add(score);
    }

    void add_undo(HBox btnRow) {
        Button undo = new Button();
        undo.setText("Undo");
        undo.setPrefWidth(75);
        undo.setOnMouseClicked(event -> {
            if (!humanTurn) {
                System.out.println("can't undo while ai running");
                return;
            }
            game.undo();
            resetTime();
        });
        btnRow.getChildren().add(undo);
    }

    void add_reset(HBox btnRow) {
        Button reset = new Button();
        reset.setText("reset");
        reset.setPrefWidth(75);
        reset.setOnMouseClicked(event -> {
            if (!humanTurn) {
                System.out.println("can't reset while ai running");
                return;
            }
            if (mode == 0)
                game.state = Game.getStandardInitialLayout();
            if (mode == 1)
                game.state = Game.getBelgianDaisyLayout();
            if (mode == 2)
                game.state = Game.getGermanDaisyLayout();
            game.reset();
            pause = true;
            humanTurn = true;
            time = 0;
            humanTime = 60;
            resetTime();
            timeLabel.setText(getTime());
        });
        btnRow.getChildren().add(reset);
    }

    void add_time_limit(HBox textRow) {
        TextField timeLimit = new TextField();
        timeLimit.setText("10000");
        textRow.getChildren().add(timeLimit);

        Button setTimeLimit = new Button();
        setTimeLimit.setText("Set AI time(ms)");
        setTimeLimit.setPrefWidth(120);
        setTimeLimit.setOnMouseClicked(event -> {
            int time = Integer.parseInt(timeLimit.getText());
            if (time > 60000) {
                System.out.println("AI time limit must be less than 60000");
                return;
            }
            game.aiTime = time;
            System.out.println("AI time set to " + time + "ms");
        });
        textRow.getChildren().add(setTimeLimit);
    }

    void counterDown() {
        time--;
    }

    String getTime() {
        return String.valueOf(time);
    }

    void resetTime() {
        if (humanTurn) time = humanTime;
        else time = (int) ((double) game.aiTime / 1000);
    }

    void add_turn_limit(HBox textRow) {
        TextField turnLimit = new TextField();
        turnLimit.setText("80");
        textRow.getChildren().add(turnLimit);

        Button setTurn = new Button();
        setTurn.setText("Set max turn (total)");
        setTurn.setPrefWidth(120);
        setTurn.setOnMouseClicked(event -> {
            try {
                int turn = Integer.parseInt(turnLimit.getText());
                if (turn > 200) {
                    System.out.println("Game turn limit must be less than 200");
                    return;
                }
                game.turnLeft = turn;
                System.out.println("Game turn limit set to " + turn);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        });
        textRow.getChildren().add(setTurn);
    }


    void add_start(HBox btnRow) {
        Button aiStart = new Button();
        aiStart.setText("AI Start");
        aiStart.setPrefWidth(120);
        aiStart.setOnMouseClicked(event -> {
            game.ai.changeTo((byte) -1);
            game.humanSide = (byte) 1;
            humanTurn = false;
            resetTime();
            pause = false;
            (new Thread(() -> {
                game.aiFirstMove();
                Platform.runLater(() -> game.update());
                humanTurn = true;
                resetTime();
                game.aiFinished = System.currentTimeMillis();
            })).start();
        });
        btnRow.getChildren().add(aiStart);

        Button humanStart = new Button();
        humanStart.setText("Human Start");
        humanStart.setPrefWidth(120);
        humanStart.setOnMouseClicked(event -> {
            game.ai.changeTo((byte) 1);
            game.humanSide = (byte) -1;
            pause = false;
            humanTurn = true;
            resetTime();
            game.aiFinished = System.currentTimeMillis();
        });
        btnRow.getChildren().add(humanStart);
    }

    void add_pause(HBox textRow) {
        Button reset = new Button();
        reset.setText("pause");
        reset.setPrefWidth(120);
        reset.setOnMouseClicked(event -> {
            if (!humanTurn) {
                System.out.println("can't pause ai");
                return;
            }
            pause = !pause;
        });
        textRow.getChildren().add(reset);
    }

    void add_standard(HBox btnRow) {
        Button standard = new Button();
        standard.setPrefWidth(100);
        standard.setText("Standard");
        standard.setOnMouseClicked(event -> {
            game.state = Game.getStandardInitialLayout();
            game.reset();
            mode = 0;
        });
        btnRow.getChildren().add(standard);
    }

    void add_bel(HBox btnRow) {
        Button bel = new Button();
        bel.setText("BelgianDaisy");
        bel.setPrefWidth(100);
        bel.setOnMouseClicked(event -> {
            game.state = Game.getBelgianDaisyLayout();
            game.reset();
            mode = 1;
        });
        btnRow.getChildren().add(bel);
    }

    void add_ger(HBox btnRow) {
        Button ger = new Button();
        ger.setText("GermanDaisy");
        ger.setPrefWidth(100);
        ger.setOnMouseClicked(event -> {
            game.state = Game.getGermanDaisyLayout();
            game.reset();
            mode = 2;
        });
        btnRow.getChildren().add(ger);
    }

}
