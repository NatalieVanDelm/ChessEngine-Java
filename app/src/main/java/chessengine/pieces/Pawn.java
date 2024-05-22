package chessengine.pieces;

import java.awt.image.BufferedImage;

import chessengine.java.Board;

public class Pawn extends Piece {
    public Pawn(Board board, int file, int rank, boolean isWhite) {
        super(board);
        this.rank = rank;
        this.file = file;
        this.xPos = file*board.tileSize;
        this.yPos = rank*board.tileSize;
        this.isWhite = isWhite;
        this.name = "Pawn";
        //this.value = 

        this.sprite = sheet.getSubimage(5*sheetScale, isWhite? 0: sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int file, int rank) {
        return true;
    }

    public boolean moveCollidesWithPiece(int file, int rank) {
        return false;
    }
}
