//
// Created by Sylvartore on 3/8/2019.
//
package sylvartore;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javax.swing.*;

public class HumanClickListener implements EventHandler<MouseEvent> {
    byte id;
    Game game;

    public HumanClickListener(int id, Game game) {
        this.id = (byte) id;
        this.game = game;
    }

    @Override
    public void handle(MouseEvent event) {
        if (game.turnLeft == 0 || game.gameOver) {
            System.out.println("game over");
            return;
        }
        if (game.state[id] != game.humanSide) return;
        String[] choices = {"Cancel", "Left", "LeftUp", "RightUp", "Right", "RightDown", "LeftDown", "Suicide-1", "Suicide-N"};
        String direction = (String) JOptionPane.showInputDialog(null, "Direction",
                "Direction", JOptionPane.QUESTION_MESSAGE, null,
                choices,
                choices[0]);
        if (direction == null || direction.equals("Cancel")) return;
        if (direction.equals("Suicide-N")) {
            game.state[id] = 0;
            game.update();
            return;
        }
        if (direction.equals("Suicide-1")) {
            game.state[id] = 0;
            game.update();
            game.ui.humanTurn = false;
            game.ui.resetTime();
            if (game.turnLeft == 0) {
                System.out.println("game over");
                return;
            }
            (new Thread(() -> {
                game.aiMove(game.ai);
                Platform.runLater(() -> game.update());
                game.ui.humanTurn = true;
                game.ui.resetTime();
                game.aiFinished = System.currentTimeMillis();
            })).start();
            return;
        }
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
        if (Game.isValidMove(id, d, n, game.state) != -1) {
            double used = (double) (System.currentTimeMillis() - game.aiFinished) / 1000;
            game.total2 += used;
            game.turnLeft--;
            String t = String.valueOf(game.total2);
            String s = String.valueOf(used);
            game.log.add("Turn Left: " + game.turnLeft
                    + "\t" + (game.humanSide == 1 ? "WHITE" : "BLACK")
                    + ": " + Game.moveToString(new byte[]{id, d, n}, game.state)
                    + "\tTime: " + (s.length() > 6 ? s.substring(0, 6) : s) + "s"
                    + "\tTotal Time: " + (t.length() > 6 ? t.substring(0, 6) : t) + "s");


            game.prev = new Game(game);
            Game.move(id, d, n, game.state);
            game.update();
            game.ui.humanTurn = false;
            game.ui.resetTime();
            if (game.turnLeft == 0) {
                System.out.println("game over");
                return;
            }
            (new Thread(() -> {
                game.aiMove(game.ai);
                Platform.runLater(() -> game.update());
                game.ui.humanTurn = true;
                game.ui.resetTime();
                game.aiFinished = System.currentTimeMillis();
            })).start();
        } else {
            System.out.println("Invalid Human Move: " + Game.ToStandardNotation[id] +
                    "(" + id + ") direction: " + Game.directionToString[d] + " N: " + N);
        }
    }


}
