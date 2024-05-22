package chessengine.pieces;

import java.awt.image.BufferedImage;

import chessengine.java.Board;

public class Rook extends Piece {
    public Rook(Board board, int file, int rank, boolean isWhite) {
        super(board);
        this.rank = rank;
        this.file = file;
        this.xPos = file*board.tileSize;
        this.yPos = rank*board.tileSize;
        this.isWhite = isWhite;
        this.name = "Rook";
        //this.value = 

        this.sprite = sheet.getSubimage(4*sheetScale, isWhite? 0: sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int file, int rank) {
        return file == this.file || rank == this.rank;
    }

    public boolean moveCollidesWithPiece(int file, int rank) {
        // check left
        if (this.file > file) {
            for (int d = this.file - 1; d > file; d--) {
                if (board.getPiece(d, rank) != null) {
                    return true;
                }
            }
        }
        // check right
        if (this.file < file) {
            for (int d = this.file + 1; d < file; d++) {
                if (board.getPiece(d, rank) != null) {
                    return true;
                }
            }
        }
        // check down
        if (this.rank < rank) {
            for (int d = this.rank + 1; d < rank; d++) {
                if (board.getPiece(file, d) != null) {
                    return true;
                }
            }
        }
        // check up
        if (this.rank > rank) {
            for (int d = this.rank - 1; d > rank; d--) {
                if (board.getPiece(file, d) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
