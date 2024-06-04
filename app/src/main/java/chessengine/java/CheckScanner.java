package chessengine.java;

import chessengine.pieces.Piece;
import java.util.ArrayList;

public class CheckScanner {
    
    Board board;
    public Piece checkedKing = null;
    public ArrayList<Piece> checkingPieces = new ArrayList<Piece>();

    public CheckScanner(Board board) {
        this.board = board;
    }

    public boolean isKingChecked(Move move) {

        Piece king = board.findKing(move.piece.isWhite);
        assert king != null;

        // have to check that the current square of the king is not in check
        int kingFile = king.file;
        int kingRank = king.rank;

        // if the king is being moved, we have to check that it's possible future square is not in check
        if(board.selectedPiece != null && board.selectedPiece.name.equals("King")) {
            kingFile = move.toFile;
            kingRank = move.toRank;
        }

        return hitByRook(move.toFile, move.toRank, king, kingFile, kingRank, 0, 1) ||
        hitByRook(move.toFile, move.toRank, king, kingFile, kingRank, 1, 0) ||
        hitByRook(move.toFile, move.toRank, king, kingFile, kingRank, 0, -1) ||
        hitByRook(move.toFile, move.toRank, king, kingFile, kingRank, -1, 0) ||

        hitByBishop(move.toFile, move.toRank, king, kingFile, kingRank, -1, -1) ||
        hitByBishop(move.toFile, move.toRank, king, kingFile, kingRank, 1,1) ||
        hitByBishop(move.toFile, move.toRank, king, kingFile, kingRank, 1,-1) ||
        hitByBishop(move.toFile, move.toRank, king, kingFile, kingRank, -1,1)  || 

        hitByKnight(move.toFile, move.toRank, king, kingFile, kingRank) || 
        hitByPawn(move.toFile, move.toRank, king, kingFile, kingRank) || 
        hitByKing(king, kingFile, kingRank);
    }

    private boolean hitByRook(int file, int rank, Piece king, int kingFile, int kingRank, int fileValue, int rankValue) {
        for (int i = 1; i < 8; i++) {
            // if piece moves to this square on the same rank/file as the king, we dont have to check this rank/file any further
            if(kingFile + (i*fileValue) == file && kingRank + (i*rankValue) == rank) {
                break;
            }

            // otherwise, we check if this square on the same rank or file as the king is occupied by a rook/queen of opposite team
            Piece piece = board.getPiece(kingFile + (i*fileValue), kingRank + (i*rankValue));
            if(piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece,king) && (piece.name.equals("Rook") || piece.name.equals("Queen"))) {
                    checkedKing = king;
                    checkingPieces.add(piece);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean hitByBishop(int file, int rank, Piece king, int kingFile, int kingRank, int fileValue, int rankValue) {
        for (int i = 1; i < 8; i++) {
            if(kingFile + (i*fileValue) == file && kingRank + (i*rankValue) == rank) {
                break;
            }

            Piece piece = board.getPiece(kingFile + (i*fileValue), kingRank + (i*rankValue));
            if(piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece,king) && (piece.name.equals("Bishop") || piece.name.equals("Queen"))) {
                    checkedKing = king;
                    checkingPieces.add(piece);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean hitByKnight(int file, int rank, Piece king, int kingFile, int kingRank) {
        // check if a knight is within reach of the king
        return checkByKnight(board.getPiece(kingFile - 1, kingRank - 2), king, file, rank) ||
        checkByKnight(board.getPiece(kingFile + 1, kingRank - 2), king, file, rank) ||
        checkByKnight(board.getPiece(kingFile - 2, kingRank - 1), king, file, rank) ||
        checkByKnight(board.getPiece(kingFile + 2, kingRank - 1), king, file, rank) ||
        checkByKnight(board.getPiece(kingFile - 1, kingRank + 2), king, file, rank) ||
        checkByKnight(board.getPiece(kingFile + 1, kingRank + 2), king, file, rank) ||
        checkByKnight(board.getPiece(kingFile - 2, kingRank + 1), king, file, rank) ||
        checkByKnight(board.getPiece(kingFile + 2, kingRank + 1), king, file, rank);
    }

    private boolean checkByKnight(Piece p, Piece king, int file, int rank) {
        // check if piece p is a Knight of the opposite team and is not going to get captured by the move.
        if(p != null && !board.sameTeam(p, king) && p.name.equals("Knight") && !(p.file == file && p.rank == rank)) {
            checkedKing = king;
            checkingPieces.add(p);
            return true;
        }
        return false;
    }

    private boolean hitByKing(Piece king, int kingFile, int kingRank) {
        // check if squares around king are occupied by king of other team
        return checkByKing(board.getPiece(kingFile - 1, kingRank -1), king) ||
        checkByKing(board.getPiece(kingFile, kingRank -1), king) ||
        checkByKing(board.getPiece(kingFile + 1, kingRank - 1), king) ||
        checkByKing(board.getPiece(kingFile + 1, kingRank), king) ||
        checkByKing(board.getPiece(kingFile + 1, kingRank + 1), king) ||
        checkByKing(board.getPiece(kingFile, kingRank + 1), king) ||
        checkByKing(board.getPiece(kingFile - 1, kingRank + 1), king) ||
        checkByKing(board.getPiece(kingFile - 1, kingRank), king);
    }

    private boolean checkByKing(Piece p, Piece king) {
        // check if piece p is a King of the opposite team
        if(p != null && !board.sameTeam(p, king) && p.name.equals("King")) {
            checkedKing = king;
            checkingPieces.add(p);
            return true;
        }
        return false;
    }

    private boolean hitByPawn(int file, int rank, Piece king, int kingFile, int kingRank) {
        int colorVal = king.isWhite ? -1 : 1;
        // check if the square diagonally right and left of king is not occupied by pawn of other team
        return checkByPawn(board.getPiece(kingFile + 1, kingRank + colorVal), king, file, rank) ||
        checkByPawn(board.getPiece(kingFile - 1, kingRank + colorVal), king, file, rank);
    }

    private boolean checkByPawn(Piece p, Piece king, int file, int rank) {
        // check if piece p is a Pawn of the opposite team and is not going to get captured by the move.
        if(p != null && !board.sameTeam(p, king) && p.name.equals("Pawn") && !(p.file == file && p.rank == rank)) {
            checkedKing = king;
            checkingPieces.add(p);
            return true;
        }
        return false;
    }

    public boolean hasLegalMoves(boolean isWhite) {
        for(Piece piece : board.pieceList) {
            if(piece.isWhite == isWhite) {
                board.selectedPiece = piece.name.equals("King") ? board.findKing(isWhite) : null;
                for (int rank = 0; rank < board.ranks; rank++) {
                    for (int file = 0; file < board.files; file++) {
                        Move move = new Move(board, piece, file, rank);
                        if(board.isValidMove(move)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
