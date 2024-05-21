package chessengine.java;

import chessengine.pieces.Piece;

public class Move {
    
    int fromFile;
    int fromRank;

    int toFile;
    int toRank;

    Piece piece;
    Piece capture;

    public Move(Board board, Piece piece, int toFile, int toRank) {
        this.fromFile = piece.file;
        this.fromRank = piece.rank;
        this.toFile = toFile;
        this.toRank = toRank;

        this.piece = piece;
        this.capture = board.getPiece(toFile, toRank);
    }
}
