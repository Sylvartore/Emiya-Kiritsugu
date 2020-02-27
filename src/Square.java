//
// Created by Sylvartore on 3/8/2019.
//
package sylvartore;

import javafx.scene.layout.Pane;
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
        slot.setVisible(false);
        slot.setRadius(40);
        getChildren().add(slot);
    }
}