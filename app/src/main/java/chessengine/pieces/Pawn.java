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
        
        int colorIndex = isWhite ? 1 : -1;

        // 1 step
        if (this.file == file && rank == this.rank - colorIndex && board.getPiece(file, rank) == null) {
            return true;
        }
        // 2 steps
        if (isFirstMove && this.file == file && rank == this.rank - 2*colorIndex && board.getPiece(file, rank) == null && board.getPiece(file, rank + colorIndex) == null) {
            return true;
        }
        // capture left
        if (this.file - 1 == file && rank == this.rank - colorIndex && board.getPiece(file, rank) != null) {
            return true;
        }
        // capture right
        if (this.file + 1 == file && rank == this.rank - colorIndex && board.getPiece(file, rank) != null) {
            return true;
        }
        // enpassant left
        if (board.getTileIndex(file, rank) == board.enPassantTile && file == this.file - 1 && rank == this.rank - colorIndex && board.getPiece(file, rank+colorIndex) != null) {
            return true;
        }
        // enpassant right
        if (board.getTileIndex(file, rank) == board.enPassantTile && file == this.file + 1 && rank == this.rank - colorIndex && board.getPiece(file, rank+colorIndex) != null) {
            return true;
        }

        return false;
    }

    public boolean moveCollidesWithPiece(int file, int rank) {
        return false;
    }
}
