package chessengine.pieces;

import java.awt.image.BufferedImage;

import chessengine.java.Board;

public class Bishop extends Piece {
    public Bishop(Board board, int file, int rank, boolean isWhite) {
        super(board);
        this.rank = rank;
        this.file = file;
        this.xPos = file*board.tileSize;
        this.yPos = rank*board.tileSize;
        this.isWhite = isWhite;
        this.name = "Bishop";
        //this.value = 

        this.sprite = sheet.getSubimage(2*sheetScale, isWhite? 0: sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int file, int rank) {
        return Math.abs(file - this.file) == Math.abs(rank - this.rank);
    }

    public boolean moveCollidesWithPiece(int file, int rank) {
        int df = this.file - file;
        int dr = this.rank - rank;
        // check left-down
        if (df > 0 && dr < 0) {
            for (int d = 1; d < df; d++) {
                if (board.getPiece(this.file - d, this.rank + d) != null) {
                    return true;
                }
            }
        }
        // check right-down
        if (df < 0 && dr < 0) {
            for (int d = 1; d < -df; d++) {
                if (board.getPiece(this.file + d, this.rank + d) != null) {
                    return true;
                }
            }
        }
        // check left-up
        if (df > 0 && dr > 0) {
            for (int d = 1; d < df; d++) {
                if (board.getPiece(this.file - d, this.rank - d) != null) {
                    return true;
                }
            }
        }
        // check right-up
        if (df < 0 && dr > 0) {
            for (int d = 1; d < -df; d++) {
                if (board.getPiece(this.file + d, this.rank - d) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
