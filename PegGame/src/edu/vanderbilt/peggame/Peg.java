package edu.vanderbilt.peggame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Peg {

    private final int SIZE = 5;
    private int[][] board = {{0},{1,1},{1,1,1},{1,1,1,1},{1,1,1,1,1}};
    private Stack<Move> solvedMoves = new Stack<Move>();
    private Stack<int [][]> solvedBoards = new Stack<int [][]>();
    
    public Peg() {
    }
    
    public boolean oneLeft() {
        int ones = 0;
        for (int row = 0; row < SIZE; row ++) {
            for (int col = 0; col < row+1; col ++) {
                if (board[row][col] == 1) {
                    ones ++;
                }
            }
        }
        return ones == 1;
    }
    
    public ArrayList<Move> checkForMove(int row, int col) {
        ArrayList<Move> moves = new ArrayList<Move>();
        if (row >= 2) {
            if (col >= 2) {
                //flat left
                if (board[row][col-1] == 1 && board[row][col-2] == 0) {
                    Move move = new Move(row,col,row,col-1,row,col-2);
                    moves.add(move);
                }
                //up left
                if (board[row-1][col-1] == 1 && board[row-2][col-2] == 0) {
                    Move move = new Move(row,col,row-1,col-1,row-2,col-2);
                    moves.add(move);
                }
            }
            if (col <= row-2) {
                //flat right
                if (board[row][col+1] == 1 && board[row][col+2] == 0) {
                    Move move = new Move(row,col,row,col+1,row,col+2);
                    moves.add(move);
                }
                //up right
                if (board[row-1][col] == 1 && board[row-2][col] == 0) {
                    Move move = new Move(row,col,row-1,col,row-2,col);
                    moves.add(move);
                }
            }
        }
        if (row <= SIZE-3) {
            //down left
            if (board[row+1][col] == 1 && board[row+2][col] == 0) {
                Move move = new Move(row,col,row+1,col,row+2,col);
                moves.add(move);
            }
            //down right
            if (board[row+1][col+1] == 1 && board[row+2][col+2] == 0) {
                Move move = new Move(row,col,row+1,col+1,row+2,col+2);
                moves.add(move);
            }
        }
        return moves;
    }
    
    public boolean solveBoard() {
        if (oneLeft()) {
            return true;
        } else {
            ArrayList<Move> moves = new ArrayList<Move>();
            for (int row = 0; row < SIZE; row ++) {
                for (int col = 0; col < row+1; col ++) {
                    if (board[row][col] == 1) {
                        ArrayList<Move> currentMoves = checkForMove(row, col);
                        moves.addAll(currentMoves);
                    }
                }
            }
            if (moves.size() == 0) {
                return false;
            } else {
                Collections.shuffle(moves);
                for (int i = 0; i < moves.size(); i ++) {
                    Move currentMove = moves.get(i);
                    int[] start = currentMove.getStart();
                    int[] jumped = currentMove.getJumped();
                    int[] end = currentMove.getEnd();
                    board[start[0]][start[1]] = 0;
                    board[jumped[0]][jumped[1]] = 0;
                    board[end[0]][end[1]] = 1;
                    int [][] curBoard = {{0},{0,0},{0,0,0},{0,0,0,0},{0,0,0,0,0}};
                    for (int row = 0; row < SIZE; row ++) {
                        for (int col = 0; col < row+1; col ++) {
                            curBoard[row][col] = board[row][col];
                        }
                    }
                    solvedMoves.push(currentMove);
                    solvedBoards.push(curBoard);
                    if (solveBoard()) {
                        return true;
                    } else {
                        board[start[0]][start[1]] = 1;
                        board[jumped[0]][jumped[1]] = 1;
                        board[end[0]][end[1]] = 0;
                        solvedMoves.pop();
                        solvedBoards.pop();
                    }
                }
                return false;
            }
        }
    }
    
    public Stack<Move> getSolvedMoves() {
        return solvedMoves;
    }
    
    public Stack<int [][]> getSolvedBoards() {
        return solvedBoards;
    }
    
    public String toString() {
        String boardStr = "";
        for (int row = 0; row < SIZE; row ++) {
            for (int col = 0; col < SIZE-row-1; col ++) {
                boardStr += " ";
            }
            for (int col = 0; col < row+1; col ++) {
                boardStr += board[row][col];
                boardStr += " ";
            }
            boardStr += "\n";
        }
        return boardStr;
    }
    
}
