package chessengine.java;

import chessengine.pieces.Piece;

public class CheckScanner {
    
    Board board;

    public CheckScanner(Board board) {
        this.board = board;
    }

    public boolean isKingChecked(Move move) {

        Piece king = board.findKing(move.piece.isWhite);
        assert king != null;

        int kingFile = king.file;
        int kingRank = king.rank;

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
            if(kingFile + (i*fileValue) == file && kingRank + (i*rankValue) == rank) {
                break;
            }

            Piece piece = board.getPiece(kingFile + (i*fileValue), kingRank + (i*rankValue));
            if(piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece,king) && (piece.name.equals("Rook") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean hitByBishop(int file, int rank, Piece king, int kingFile, int kingRank, int fileValue, int rankValue) {
        for (int i = 1; i < 8; i++) {
            if(kingFile - (i*fileValue) == file && kingRank - (i*rankValue) == rank) {
                break;
            }

            Piece piece = board.getPiece(kingFile - (i*fileValue), kingRank - (i*rankValue));
            if(piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece,king) && (piece.name.equals("Bishop") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean hitByKnight(int file, int rank, Piece king, int kingFile, int kingRank) {
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
        return p != null && !board.sameTeam(p, king) && p.name.equals("Knight") && !(p.file == file && p.rank == rank);
    }

    private boolean hitByKing(Piece king, int kingFile, int kingRank) {
        return checkByKing(board.getPiece(kingFile - 1, kingRank -1), king) ||
        checkByKing(board.getPiece(kingFile + 1, kingRank -1), king) ||
        checkByKing(board.getPiece(kingFile - 1, kingRank +1), king) ||
        checkByKing(board.getPiece(kingFile + 1, kingRank + 1), king) ||
        checkByKing(board.getPiece(kingFile - 1, kingRank), king) ||
        checkByKing(board.getPiece(kingFile + 1, kingRank), king) ||
        checkByKing(board.getPiece(kingFile, kingRank + 1), king) ||
        checkByKing(board.getPiece(kingFile, kingRank -1), king);
    }

    private boolean checkByKing(Piece p, Piece king) {
        return p != null && !board.sameTeam(p, king) && p.name.equals("King");
    }

    private boolean hitByPawn(int file, int rank, Piece king, int kingFile, int kingRank) {
        int colorVal = king.isWhite ? -1 : 1;
        return checkByPawn(board.getPiece(kingFile + 1, kingRank + colorVal), king, file, rank) ||
        checkByPawn(board.getPiece(kingFile - 1, kingRank + colorVal), king, file, rank);
    }

    private boolean checkByPawn(Piece p, Piece king, int file, int rank) {
        return p != null && !board.sameTeam(p, king) && p.name.equals("Pawn") && !(p.file == file && p.rank == rank);
    }

    public boolean isGameOver(Piece king) {
        for(Piece piece : board.pieceList) {
            if(board.sameTeam(piece, king)) {
                board.selectedPiece = piece == king ? king : null;
                for (int rank = 0; rank < board.ranks; rank++) {
                    for (int file = 0; file < board.files; file++) {
                        Move move = new Move(board, piece, file, rank);
                        if(board.isValidMove(move)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
