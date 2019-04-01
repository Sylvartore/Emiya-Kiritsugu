package sylvartore;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class UI extends Application {

    private VBox r;
    private GridPane root;
    Square[][] squares;

    public static void main(String[] args) {
        launch(args);
    }

    private void readLayout(BitBoard1D board) {
        try {
            FileReader fr = new FileReader("src/test_input/Test7.input");
            BufferedReader br = new BufferedReader(fr);
            //  BufferedReader br = new BufferedReader(new StringReader(BitBoard1D.randomStateGenerator()));
            String line;
            int side = 10;
            for (int i = 1; (line = br.readLine()) != null; i++) {
                System.out.println(line);
                if (i == 1) {
                    if (line.toLowerCase().charAt(0) == 'w') side = 1;
                } else {
                    String[] states = line.split(",");
                    for (String state : states) {
                        board.readState(state.trim());
                    }
                }
            }
//            Set<String> ans = new TreeSet<>();
//            List<BitBoard1D> moves = board.getAllPossibleMoves((byte) side);
//            for (BitBoard1D move : moves) {
//                ans.add(move.stateToString());
//            }
//            FileWriter fw = new FileWriter("src/test_res/Test3.board");
//            BufferedWriter bw = new BufferedWriter(fw);
//            for (String s : ans) {
//                bw.write(s + "\n");
//            }
//            bw.close();
//
//            br = new BufferedReader(new FileReader("src/test_input/Test2.board"));
//            int count = 0;
//            while ((line = br.readLine()) != null) {
//                if (ans.contains(line)) System.out.println("Failed: " + line);
//                count++;
//            }
//            if (ans.size() == count) System.out.println("Passed!");
//            else System.out.println("Failed: redundant answers");


//            fr.close();
//            br.close();
//            fw.close();
//            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Abalone");
        r = new VBox();
        root = new GridPane();
        squares = new Square[20][20];
        BitBoard1D b = new BitBoard1D();
//        readLayout(b);
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new Square(-1, i, j);
                root.add(squares[i][j], j, i);
            }
        }
        b.state = b.getStandardInitialLayout();
        b.init(squares, bs, ws);
        b.show();
        r.getChildren().add(root);
        add_btn(b);
        Scene mainScene = new Scene(r, 800, 1000);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    void add_btn(BitBoard1D b) {
        HBox btnRow = new HBox();
        Button standard = new Button();
        standard.setPrefWidth(100);
        standard.setText("Standard");
        standard.setOnMouseClicked(event -> {
            b.state = b.getStandardInitialLayout();
            b.humanSide = (byte) 10;
            b.aiSide = (byte) 1;
            b.total = 0;
            b.turnLeft = 80;
            b.aiTime = 3000;
            b.prev = null;
            b.show();
        });

        btnRow.getChildren().add(standard);

        Button bel = new Button();
        bel.setText("BelgianDaisy");
        bel.setPrefWidth(100);
        bel.setOnMouseClicked(event -> {
            b.state = b.getBelgianDaisyLayout();
            b.humanSide = (byte) 10;
            b.aiSide = (byte) 1;
            b.total = 0;
            b.aiTime = 3000;
            b.turnLeft = 80;
            b.prev = null;
            b.show();
        });
        btnRow.getChildren().add(bel);

        Button ger = new Button();
        ger.setText("GermanDaisy");
        ger.setPrefWidth(100);
        ger.setOnMouseClicked(event -> {
            b.humanSide = (byte) 10;
            b.aiSide = (byte) 1;
            b.total = 0;
            b.aiTime = 3000;
            b.turnLeft = 80;
            b.prev = null;
            b.state = b.getGermanDaisyLayout();
            b.show();
        });
        btnRow.getChildren().add(ger);

        Button aiGofirst = new Button();
        aiGofirst.setText("AI go first");
        aiGofirst.setPrefWidth(120);
        aiGofirst.setOnMouseClicked(event -> {
            b.aiSide = (byte) 10;
            b.humanSide = (byte) 1;
            b.aiMove();
        });
        btnRow.getChildren().add(aiGofirst);

        Button undo = new Button();
        undo.setText("Undo");
        undo.setPrefWidth(75);
        undo.setOnMouseClicked(event -> {
            b.undo();
        });
        btnRow.getChildren().add(undo);
        r.getChildren().add(btnRow);

        HBox textRow = new HBox();

        TextField timeLimit = new TextField();
        timeLimit.setText("3000");
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
                b.aiTime = t;
                System.out.println("AI turn time set to " + t + "ms");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        textRow.getChildren().add(setTimeLimit);

        TextField turn = new TextField();
        turn.setText("60");
        textRow.getChildren().add(turn);

        Button setTurn = new Button();
        setTurn.setText("Set max turn (total)");
        setTurn.setPrefWidth(120);
        setTurn.setOnMouseClicked(event -> {
            try {
                int t = Integer.parseInt(turn.getText());
                if (t % 2 != 0) {
                    System.out.println("Game turn limit must be an even integer");
                    return;
                }
                if (t > 200) {
                    System.out.println("Game turn limit must be less than 200");
                    return;
                }
                b.turnLeft = t;
                System.out.println("Game turn limit set to " + t);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        });
        textRow.getChildren().add(setTurn);

        r.getChildren().add(textRow);

        HBox score = new HBox();
        bs = new Text();
        bs.setText("Black Score: 0      ");
        score.getChildren().add(bs);
        ws = new Text();
        ws.setText("White Score: 0      ");
        score.getChildren().add(ws);
        r.getChildren().add(score);
        b.init(squares, bs, ws);
    }

    Text ws;
    Text bs;

    class Square extends Pane {
        int id;
        int col;
        int row;

        public Square(int id, int col, int row) {
            this.id = id;
            this.col = col;
            this.row = row;
            setWidth(60);
            setHeight(60);
        }
    }
}
