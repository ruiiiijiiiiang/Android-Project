package edu.vanderbilt.peggame;

public class Move {

    private int[] start = new int[2];
    private int[] jumped = new int[2];
    private int[] end = new int[2];
    
    public Move(int startRow, int startCol,
                int jumpedRow, int jumpedCol,
                int endRow, int endCol) {
        start[0] = startRow;
        start[1] = startCol;
        jumped[0] = jumpedRow;
        jumped[1] = jumpedCol;
        end[0] = endRow;
        end[1] = endCol;
    }
    
    public int[] getStart() {
        return start;
    }
    
    public int[] getJumped() {
        return jumped;
    }
    
    public int[] getEnd() {
        return end;
    }
    
    public String toString() {
        String moveStr = "";
        moveStr += Integer.toString(start[0]) + Integer.toString(start[1]) + Integer.toString(jumped[0]) + Integer.toString(jumped[1]) + Integer.toString(end[0]) + Integer.toString(end[1]);
        return moveStr;
    }
}
