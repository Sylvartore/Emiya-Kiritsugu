package sylvartore;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class Square extends Pane {
    int id;
    int col;
    int row;
    Circle slot;

    public Square(int id, int col, int row) {
        this.id = id;
        this.col = col;
        this.row = row;
        setWidth(60);
        setHeight(60);
        slot = new Circle();
        slot.setFill(Color.YELLOW);
        slot.setRadius(20);
        getChildren().add(slot);
    }
}