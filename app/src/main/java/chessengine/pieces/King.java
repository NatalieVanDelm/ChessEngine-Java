package chessengine.pieces;

import java.awt.image.BufferedImage;

import chessengine.java.Board;

public class King extends Piece {
    public King(Board board, int file, int rank, boolean isWhite) {
        super(board);
        this.rank = rank;
        this.file = file;
        this.xPos = file*board.tileSize;
        this.yPos = rank*board.tileSize;
        this.isWhite = isWhite;
        this.name = "King";
        //this.value = 

        this.sprite = sheet.getSubimage(0*sheetScale, isWhite? 0: sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isValidMovement(int file, int rank) {
        if (Math.abs(file - this.file) == 1 && Math.abs(rank - this.rank) == 1) {
            return true;
        }
        if (Math.abs(file - this.file) + Math.abs(rank - this.rank) == 1) {
            return true;
        }
        return false;
    }

    public boolean moveCollidesWithPiece(int file, int rank) {
        return false;
    }
}
