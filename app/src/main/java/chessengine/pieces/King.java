package chessengine.pieces;

import java.awt.image.BufferedImage;

import chessengine.java.Board;
import chessengine.java.Move;

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
        if ((Math.abs(file - this.file) == 1 && Math.abs(rank - this.rank) == 1) || (Math.abs(file - this.file) + Math.abs(rank - this.rank) == 1) || canCastle(file, rank)) {
            return true;
        }
        return false;
    }

    public boolean moveCollidesWithPiece(int file, int rank) {
        return false;
    }

    private boolean canCastle(int file, int rank) {
        if (this.rank == rank) {
            if (file == 6) {
                Piece rook = board.getPiece(7, rank);
                if (rook != null && rook.isFirstMove && isFirstMove) {
                    return board.getPiece(5, rank) == null &&
                    board.getPiece(6, rank) == null &&
                    !board.checkScanner.isKingChecked(new Move(board, this, 5, rank));
                }
            } else if (file == 2) {
                Piece rook = board.getPiece(0, rank);
                if (rook != null && rook.isFirstMove && isFirstMove) {
                    return board.getPiece(1, rank) == null &&
                    board.getPiece(2, rank) == null &&
                    board.getPiece(3, rank) == null &&
                    !board.checkScanner.isKingChecked(new Move(board, this, 3, rank));
                }
            }
        }
        return false;
    }
}
