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
}
