package chessengine.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.*;

import chessengine.pieces.*;

public class Board extends JPanel{

    Input input = new Input(this);

    public int tileSize = 85;

    int ranks = 8; // rows
    int files = 8; // columns

    Piece selectedPiece;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Board() {
        this.setPreferredSize(new Dimension(files*tileSize, ranks*tileSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

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

    public boolean isValidMove(Move move) {
        if(sameTeam(move.piece, move.capture)) {
            return false;
        }
        if (!move.piece.isValidMovement(move.toFile, move.toRank)) {
            return false;
        }
        if(move.piece.moveCollidesWithPiece(move.toFile, move.toRank)) {
            return false;
        }
        return true;
    }

    private boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public void makeMove(Move move) {
        if(this.isValidMove(move)){
            move.piece.file = move.toFile;
            move.piece.rank = move.toRank;
            move.piece.xPos = move.toFile*tileSize;
            move.piece.yPos = move.toRank*tileSize;
            
            this.capture(move);
        }
    }

    public void capture(Move move) {
        pieceList.remove(move.capture);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int r = 0; r < ranks; r++) {
            for (int f = 0; f < files; f++) {
                g2d.setColor((f+r)%2 == 0 ? new Color(227,190,181) : new Color(157,105,53));
                g2d.fillRect(f*tileSize, r*tileSize, tileSize, tileSize);
            }
        }

        if(selectedPiece != null) {
            for (int r = 0; r < ranks; r++) {
                for (int f = 0; f < files; f++) {
                    if(isValidMove(new Move(this, selectedPiece, f, r))) {
                        g2d.setColor(new Color(68,100,57,240));
                        g2d.fillRect(f*tileSize, r*tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        for(Piece piece : pieceList) {
            piece.paint(g2d);
        }
    }
    
}
