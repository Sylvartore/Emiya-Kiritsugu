package sylvartore;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;

public class UI extends Application {

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
        root = new GridPane();
        squares = new Square[20][20];
        BitBoard1D b = new BitBoard1D();
//        readLayout(b);
        b.state = b.getStandardInitialLayout();
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
