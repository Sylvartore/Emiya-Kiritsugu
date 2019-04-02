package sylvartore;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javax.swing.*;

public class HumanClickListener implements EventHandler<MouseEvent> {
    byte id;
    Board board;

    public HumanClickListener(int id, Board board) {
        this.id = (byte) id;
        this.board = board;
    }

    @Override
    public void handle(MouseEvent event) {
        if (board.turnLeft == 0 || board.gameOver) {
            System.out.println("game over");
            return;
        }
        if (board.state[id] != board.humanSide) return;
        String[] choices = {"Cancel", "Left", "LeftUp", "RightUp", "Right", "RightDown", "LeftDown"};
        String direction = (String) JOptionPane.showInputDialog(null, "Direction",
                "Direction", JOptionPane.QUESTION_MESSAGE, null,
                choices,
                choices[0]);
        if (direction == null || direction.equals("Cancel")) return;
        byte d = 0;
        for (byte i = 1; i < choices.length; i++) {
            if (choices[i].equals(direction)) d = (byte) (i - 1);
        }
        choices = new String[]{"1", "2", "3"};

        String N = (String) JOptionPane.showInputDialog(null, "N",
                "N", JOptionPane.QUESTION_MESSAGE, null,
                choices,
                choices[0]);
        if (N == null) return;
        byte n = Byte.parseByte(N);
        if (Board.isValidMove(id, d, n, board.state) != -1) {
            board.prev = new Board(board);
            board.turnLeft--;
            Board.move(id, d, n, board.state);
            board.update();
            if (board.turnLeft == 0) {
                System.out.println("game over");
                return;
            }
            (new Thread(() -> {
                board.aiMove(board.ai);
                Platform.runLater(() -> {
                    board.update();
                });
            })).start();
        } else {
            System.out.println("Invalid Human Move: " + Board.ToStandardNotation[id] +
                    "(" + id + ") direction: " + Board.directionToString[d] + " N: " + N);
        }
    }


}
