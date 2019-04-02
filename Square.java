package sylvartore;

import javafx.scene.layout.Pane;

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