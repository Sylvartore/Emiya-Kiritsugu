package sylvartore;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class UI extends Application {

    private GridPane root;
    Square[][] squares;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Abalone");
        root = new GridPane();
        squares = new Square[20][20];
        BitBoard1D b = new BitBoard1D();
        //Board b = new Board();
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
