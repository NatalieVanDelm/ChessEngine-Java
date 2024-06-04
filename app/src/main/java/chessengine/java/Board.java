package chessengine.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.*;

import chessengine.pieces.*;

public class Board extends JPanel {

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public String fenStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public int tileSize = 85;

    int ranks = 8; // rows
    int files = 8; // columns

    Piece selectedPiece;

    public int enPassantTile = -1;

    ArrayList<Piece> pieceList = new ArrayList<>();

    private boolean isWhiteToMove = true;
    private boolean isGameOver = false;

    public Board() {
        this.setPreferredSize(new Dimension(files*tileSize, ranks*tileSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        this.loadPiecesFromFen(fenStartingPosition);
    }

    public int getTileIndex(int file, int rank) {
        return file + rank * ranks;
    }

    public Piece findKing(boolean isWhite) {
        for(Piece piece : pieceList) {
            if(piece.isWhite == isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        return null;
    }

    public void loadPiecesFromFen(String fenString) {
        pieceList.clear();
        String[] parts = fenString.split(" ");

        // adding pieces
        String position = parts[0];
        int rank = 0;
        int file = 0;
        for (int i = 0; i < position.length(); i++) {
            char ch = position.charAt(i);
            if(ch == '/') {
                rank++;
                file = 0;
            } else if (Character.isDigit(ch)) {
                file += Character.getNumericValue(ch);
            } else {
                boolean isWhite = Character.isUpperCase(ch);
                char pieceChar = Character.toLowerCase(ch);

                switch (pieceChar) {
                    case 'r':
                        pieceList.add(new Rook(this, file, rank, isWhite));
                        break;
                    case 'k':
                        pieceList.add(new King(this, file, rank, isWhite));
                        break;
                    case 'n':
                        pieceList.add(new Knight(this, file, rank, isWhite));
                        break;
                    case 'q':
                        pieceList.add(new Queen(this, file, rank, isWhite));
                        break;
                    case 'b':
                        pieceList.add(new Bishop(this, file, rank, isWhite));
                        break;
                    case 'p':
                        pieceList.add(new Pawn(this, file, rank, isWhite));
                        break;
                }

                file++;
            }
        }

        // deciding turn
        isWhiteToMove = parts[1].equals("w");

        // castling rights
        String castlingRights = parts[2];
        Piece bqr = getPiece(0, 0);
        if(bqr instanceof Rook) {
            bqr.isFirstMove = castlingRights.contains("q");
        }
        Piece bkr = getPiece(7, 0);
        if(bkr instanceof Rook) {
            bkr.isFirstMove = castlingRights.contains("k");
        }
        Piece wqr = getPiece(0, 7);
        if(wqr instanceof Rook) {
            wqr.isFirstMove = castlingRights.contains("q");
        }
        Piece wkr = getPiece(7, 7);
        if(wkr instanceof Rook) {
            wkr.isFirstMove = castlingRights.contains("k");
        }

        // en passant square
        String enPassant = parts[3];
        if(enPassant.equals("-")) {
            enPassantTile = -1;
        } else {
            enPassantTile = (7 - (enPassant.charAt(1) - '1')) * 8 + (enPassant.charAt(0) - 'a');
        }
    }

    public Piece getPiece(int file, int rank) {
        for(Piece piece : pieceList) {
            if(piece.rank == rank && piece.file == file) {
                return piece;
            }
        }
        return null;
    }

    public boolean isValidMove(Move move) {
        if(isGameOver) {
            return false;
        }
        if(isWhiteToMove != move.piece.isWhite){
            return false;
        }
        if(sameTeam(move.piece, move.capture)) {
            return false;
        }
        if (!move.piece.isValidMovement(move.toFile, move.toRank)) {
            return false;
        }
        if(move.piece.moveCollidesWithPiece(move.toFile, move.toRank)) {
            return false;
        }
        if(checkScanner.isKingChecked(move)) {
            return false;
        }
        return true;
    }

    public boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    private void promotePawn(Move move) {
        pieceList.add(new Queen(this, move.toFile, move.toRank, move.piece.isWhite));
        capture(move.piece);
    }

    public void makeMove(Move move) {
        if(move.piece.name.equals("Pawn")) {
            int colorIndex = move.piece.isWhite ? 1 : -1;

            if (getTileIndex(move.toFile, move.toRank) == enPassantTile) {
                move.capture = getPiece(move.toFile, move.toRank + colorIndex);
            }
            if(Math.abs(move.piece.rank - move.toRank) == 2) {
                enPassantTile = getTileIndex(move.toFile, move.toRank + colorIndex);
            } else {
                enPassantTile = -1;
            }

            // promotions
            colorIndex = move.piece.isWhite ? 0 : 7;
            if(move.toRank == colorIndex) {
                promotePawn(move);
            }
        } else {
            enPassantTile = -1;
        }
        
        if (move.piece.name.equals("King")) {
            if(Math.abs(move.piece.file - move.toFile) == 2) {
                Piece rook;
                if(move.piece.file < move.toFile) {
                    rook = getPiece(7, move.piece.rank);
                    rook.file = 5;
                } else {
                    rook = getPiece(0, move.piece.rank);
                    rook.file = 3;
                }
                rook.xPos = rook.file*tileSize;
            }
        }

        move.piece.file = move.toFile;
        move.piece.rank = move.toRank;
        move.piece.xPos = move.toFile*tileSize;
        move.piece.yPos = move.toRank*tileSize;

        move.piece.isFirstMove = false;
        
        this.capture(move.capture);

        isWhiteToMove = !isWhiteToMove;

        System.out.println("Updating game state.");
        updateGameState();
    }

    public void capture(Piece piece) {
        pieceList.remove(piece);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int r = 0; r < ranks; r++) {
            for (int f = 0; f < files; f++) {
                g2d.setColor((f+r)%2 == 0 ? new Color(227,190,181) : new Color(157,105,53));
                g2d.fillRect(f*tileSize, r*tileSize, tileSize, tileSize);
            }
        }

        if(selectedPiece != null) {
            for (int r = 0; r < ranks; r++) {
                for (int f = 0; f < files; f++) {
                    if(isValidMove(new Move(this, selectedPiece, f, r))) {
                        g2d.setColor(new Color(68,100,57,240));
                        g2d.fillRect(f*tileSize, r*tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        for(Piece piece : pieceList) {
            piece.paint(g2d);
        }
    }

    private boolean insufficientMaterial(boolean isWhite) {
        ArrayList<String> names = pieceList.stream()
            .filter(p -> p.isWhite == isWhite)
            .map(p -> p.name)
            .collect(Collectors.toCollection(ArrayList::new));
        if(names.contains("Queen") || names.contains("Pawn") || names.contains("Rook")) {
            return false;
        }
        return names.size() < 3;
    }

    private void updateGameState() {
        Piece king = findKing(isWhiteToMove);

        if(!checkScanner.hasLegalMoves(isWhiteToMove)) {
            if(checkScanner.isKingChecked(new Move(this, king, king.file, king.rank))) {
                System.out.println("Checkmate.");
                for(Piece piece : checkScanner.checkingPieces) {
                    System.out.println(checkScanner.checkedKing.isWhite ? "White king in check by " + piece.name : "Black king in check by " + piece.name);
                }
                isGameOver = true;
            } else {
                System.out.println("Stalemate.");
                System.out.println(king.isWhite ? "White doesnt have any legal moves but is not in check." : "Black doesnt have any legal moves but is not in check.");
                isGameOver = true;
            }
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) {
            System.out.println("Insufficient material.");
            isGameOver = true;
        }
    }
}
