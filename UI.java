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
        Board b = new Board();
      /*  Circle e= new Circle();
        Circle c = new Circle();
        c.setRadius(70);
        Pane a = new Pane();
        a.getChildren().add(c);*/

//        ImageView b = new ImageView(new Image("black-ball.png"));
//        b.setFitHeight(80);
//        b.setFitWidth(80);
//        root.add(b, 1, 0);
//        ImageView a = new ImageView(new Image("black-ball.png"));
//        a.setFitHeight(80);
//        a.setFitWidth(80);
//        root.add(a, 2, 0);
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new Square(i, j);
                root.add(squares[i][j], j, i);
            }
        }

        b.show(squares);
        Scene mainScene = new Scene(root, 800, 1000);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    class Square extends Pane {
        int row;
        int col;

        public Square(int row, int col) {
            this.row = row;
            this.col = col;
            setWidth(60);
            setHeight(60);
        }
    }



}
