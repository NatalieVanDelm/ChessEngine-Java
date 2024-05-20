package chessengine.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

public class Board extends JPanel{

    public int tileSize = 85;

    int ranks = 8; // rows
    int files = 8; // columns

    public Board() {
        this.setPreferredSize(new Dimension(files*tileSize, ranks*tileSize));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int r = 0; r < ranks; r++) {
            for (int f = 0; f < files; f++) {
                g2d.setColor((f+r)%2 == 0 ? new Color(227,190,181) : new Color(157,105,53));
                g2d.fillRect(f*tileSize, r*tileSize, tileSize, tileSize);
            }
        }
    }
    
}
