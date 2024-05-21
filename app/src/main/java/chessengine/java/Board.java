package chessengine.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.*;

import chessengine.pieces.*;

public class Board extends JPanel{

    public int tileSize = 85;

    int ranks = 8; // rows
    int files = 8; // columns

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Board() {
        this.setPreferredSize(new Dimension(files*tileSize, ranks*tileSize));
        this.addPieces();
    }

    public void addPieces() {
        // setup black
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));

        for(int f = 0; f < files; f++) {
            pieceList.add(new Pawn(this, f, 1, false));
        }

        // setup white
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));

        for(int f = 0; f < files; f++) {
            pieceList.add(new Pawn(this, f, 6, true));
        }
    }

    public Piece getPiece(int file, int rank) {
        for(Piece piece : pieceList) {
            if(piece.rank == rank && piece.file == file) {
                return piece;
            }
        }
        return null;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int r = 0; r < ranks; r++) {
            for (int f = 0; f < files; f++) {
                g2d.setColor((f+r)%2 == 0 ? new Color(227,190,181) : new Color(157,105,53));
                g2d.fillRect(f*tileSize, r*tileSize, tileSize, tileSize);
            }
        }

        for(Piece piece : pieceList) {
            piece.paint(g2d);
        }
    }
    
}
