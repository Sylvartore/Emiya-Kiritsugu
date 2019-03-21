package sylvartore;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class UI extends Application {

    private GridPane root;
    Square[][] squares;

    public static void main(String[] args) {
        launch(args);
    }

    private void readLayout(BitBoard1D board) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/test_input/Test2.input"));
            String line;
            int side = 10;
            for (int i = 1; (line = br.readLine()) != null; i++) {
                if (i == 1) {
                    if (line.toLowerCase().charAt(0) == 'w') side = 1;
                } else {
                    String[] states = line.split(",");
                    for (String state : states) {
                        board.readState(state);
                    }
                }
            }
            Set<String> ans = new TreeSet<>();
            List<BitBoard1D> moves = board.getAllPossibleMoves((byte) side);
            for (BitBoard1D move : moves) {
                List<String> note = new ArrayList<>();
                for (byte i = 0; i < move.state.length; i++) {
                    if (move.state[i] != 0) {
                        String sideStr = move.state[i] == 1 ? "w" : "b";
                        note.add(BitBoard1D.toStandardNotation[i] + sideStr);
                    }
                }
                note.sort((a, b) -> {
                    if (a.charAt(2) == 'b' && b.charAt(2) == 'w') return -1;
                    if (a.charAt(2) == 'w' && b.charAt(2) == 'b') return 1;
                    return a.compareTo(b);
                });
                String encode = note.toString();
                ans.add(encode.substring(1, encode.length() - 1));
            }
            br = new BufferedReader(new FileReader("src/test_key/Test2.board"));
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (ans.contains(line)) System.out.println("Failed: " + line);
                count++;
            }
            if (ans.size() == count) System.out.println("Passed!");
            else System.out.println("Failed: redundant answers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Abalone");
        root = new GridPane();
        squares = new Square[20][20];
        BitBoard1D b = new BitBoard1D();
        readLayout(b);
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new Square(-1, i, j);
                root.add(squares[i][j], j, i);
            }
        }
        b.init(squares);
        b.show();
        Scene mainScene = new Scene(root, 800, 1000);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

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
