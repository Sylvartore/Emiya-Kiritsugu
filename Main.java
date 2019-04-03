package sylvartore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private VBox root;
    private GridPane board;
    Square[][] squares;
    Board game_board;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Abalone");
        root = new VBox();
        board = new GridPane();
        squares = new Square[17][18];
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new Square(-1, i, j);
                board.add(squares[i][j], j, i);
            }
        }

        game_board = new Board();
        game_board.init(squares);
        game_board.update();
        root.getChildren().add(board);
        add_btn();
        Scene mainScene = new Scene(root, 800, 1000);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    void add_btn() {
        HBox btnRow = new HBox();
        add_standard(btnRow);
        add_bei(btnRow);
        add_ger(btnRow);
        add_aiGo(btnRow);
        add_undo(btnRow);
        add_ava(btnRow);
        //   add_ko5(btnRow);
        root.getChildren().add(btnRow);
        HBox textRow = new HBox();
        add_time_limit(textRow);
        add_turn_limit(textRow);
        add_pause(textRow);
        root.getChildren().add(textRow);
        add_scores();
    }

    void add_ava(HBox btnRow) {
        Button AivAi = new Button();
        AivAi.setText("AI vs AI");
        AivAi.setPrefWidth(120);
        AivAi.setOnMouseClicked(event -> {
            mainAiMove();
            //counterAiMove();
        });
        btnRow.getChildren().add(AivAi);
    }

    void add_ko5(HBox btnRow) {
        Button ko5 = new Button();
        ko5.setText("Ko5 up");
        ko5.setPrefWidth(120);
        ko5.setOnMouseClicked(event -> {
            game_board.ai = new KingOf5Sec(game_board.ai.side, "Ko5");
        });
        btnRow.getChildren().add(ko5);
    }

    void mainAiMove() {
        (new Thread(() -> {
            game_board.aiMove(game_board.ai);
            Platform.runLater(() -> {
                game_board.update();
                if (game_board.turnLeft == 0 || game_board.gameOver) return;
                counterAiMove();
            });
        })).start();
    }

    void counterAiMove() {
        (new Thread(() -> {
            game_board.aiMove(game_board.counter);
            Platform.runLater(() -> {
                game_board.update();
                if (game_board.turnLeft == 0 || game_board.gameOver) return;
                mainAiMove();
            });
        })).start();
    }

    void add_scores() {
        HBox score = new HBox();
        Text bs = new Text();
        bs.setText("Black Score: 0      ");
        score.getChildren().add(bs);
        Text ws = new Text();
        ws.setText("White Score: 0      ");
        score.getChildren().add(ws);
        root.getChildren().add(score);
        game_board.ws = ws;
        game_board.bs = bs;
    }

    void add_undo(HBox btnRow) {
        Button undo = new Button();
        undo.setText("Undo");
        undo.setPrefWidth(75);
        undo.setOnMouseClicked(event -> {
            game_board.undo();
        });
        btnRow.getChildren().add(undo);
    }

    void add_time_limit(HBox textRow) {
        TextField timeLimit = new TextField();
        timeLimit.setText("5000");
        textRow.getChildren().add(timeLimit);

        Button setTimeLimit = new Button();
        setTimeLimit.setText("Set AI time(ms)");
        setTimeLimit.setPrefWidth(120);
        setTimeLimit.setOnMouseClicked(event -> {
            try {
                int t = Integer.parseInt(timeLimit.getText());
                if (t > 60000) {
                    System.out.println("Game turn limit must be less than 60000");
                    return;
                }
                game_board.aiTime = t;
                System.out.println("AI turn time set to " + t + "ms");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        textRow.getChildren().add(setTimeLimit);
    }

    void add_turn_limit(HBox textRow) {
        TextField turn = new TextField();
        turn.setText("100");
        textRow.getChildren().add(turn);

        Button setTurn = new Button();
        setTurn.setText("Set max turn (total)");
        setTurn.setPrefWidth(120);
        setTurn.setOnMouseClicked(event -> {
            try {
                int t = Integer.parseInt(turn.getText());
                if (t > 200) {
                    System.out.println("Game turn limit must be less than 200");
                    return;
                }
                game_board.turnLeft = t;
                System.out.println("Game turn limit set to " + t);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        });
        textRow.getChildren().add(setTurn);
    }


    void add_aiGo(HBox btnRow) {
        Button aiGofirst = new Button();
        aiGofirst.setText("AI go first");
        aiGofirst.setPrefWidth(120);
        aiGofirst.setOnMouseClicked(event -> {
            game_board.ai.side = (byte) -1;
            game_board.humanSide = (byte) 1;
            game_board.aiMove(game_board.ai);
            game_board.update();
        });
        btnRow.getChildren().add(aiGofirst);
    }

    void add_pause(HBox textRow) {
        Button reset = new Button();
        reset.setText("pause");
        reset.setPrefWidth(120);
        reset.setOnMouseClicked(event -> {
        });
        textRow.getChildren().add(reset);
    }

    void add_standard(HBox btnRow) {
        Button standard = new Button();
        standard.setPrefWidth(100);
        standard.setText("Standard");
        standard.setOnMouseClicked(event -> {
            game_board.state = game_board.getStandardInitialLayout();
            game_board.reset();
        });
        btnRow.getChildren().add(standard);
    }

    void add_bei(HBox btnRow) {
        Button bel = new Button();
        bel.setText("BelgianDaisy");
        bel.setPrefWidth(100);
        bel.setOnMouseClicked(event -> {
            game_board.state = game_board.getBelgianDaisyLayout();
            game_board.reset();
        });
        btnRow.getChildren().add(bel);
    }

    void add_ger(HBox btnRow) {
        Button ger = new Button();
        ger.setText("GermanDaisy");
        ger.setPrefWidth(100);
        ger.setOnMouseClicked(event -> {
            game_board.state = game_board.getGermanDaisyLayout();
            game_board.reset();
        });
        btnRow.getChildren().add(ger);
    }

}
