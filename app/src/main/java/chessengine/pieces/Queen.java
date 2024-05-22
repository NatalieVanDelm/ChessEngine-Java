package chessengine.pieces;

import java.awt.image.BufferedImage;

import chessengine.java.Board;

public class Queen extends Piece {
    public Queen(Board board, int file, int rank, boolean isWhite) {
        super(board);
        this.rank = rank;
        this.file = file;
        this.xPos = file*board.tileSize;
        this.yPos = rank*board.tileSize;
        this.isWhite = isWhite;
        this.name = "Queen";
        //this.value = 

        this.sprite = sheet.getSubimage(1*sheetScale, isWhite? 0: sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int file, int rank) {
        // moves like rook
        if (file == this.file || rank == this.rank) {
            return true;
        }
        // moves like bishop
        if (Math.abs(file - this.file) == Math.abs(rank - this.rank)) {
            return true;
        }
        return false;
    }

    public boolean moveCollidesWithPiece(int file, int rank) {
        // moves like rook
        if (file == this.file || rank == this.rank) {
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
        } else {
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
        }
        return false;
    }
}
