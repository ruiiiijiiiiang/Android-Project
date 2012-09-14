package edu.vanderbilt.peggame;

import java.util.Stack;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    final int SIZE = 5;
    int curMovNum = -1;
    Stack<Move> solvedMoves = new Stack<Move>();
    Stack<int [][]> solvedBoards = new Stack<int [][]>();
    ImageView[][] board;
    TextView text;
    Drawable emptyDraw;
    Drawable occupiedDraw;
    Drawable startDraw;
    Drawable endDraw;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ImageView peg00 = (ImageView) findViewById(R.id.peg00);
        ImageView peg10 = (ImageView) findViewById(R.id.peg10);
        ImageView peg11 = (ImageView) findViewById(R.id.peg11);
        ImageView peg20 = (ImageView) findViewById(R.id.peg20);
        ImageView peg21 = (ImageView) findViewById(R.id.peg21);
        ImageView peg22 = (ImageView) findViewById(R.id.peg22);
        ImageView peg30 = (ImageView) findViewById(R.id.peg30);
        ImageView peg31 = (ImageView) findViewById(R.id.peg31);
        ImageView peg32 = (ImageView) findViewById(R.id.peg32);
        ImageView peg33 = (ImageView) findViewById(R.id.peg33);
        ImageView peg40 = (ImageView) findViewById(R.id.peg40);
        ImageView peg41 = (ImageView) findViewById(R.id.peg41);
        ImageView peg42 = (ImageView) findViewById(R.id.peg42);
        ImageView peg43 = (ImageView) findViewById(R.id.peg43);
        ImageView peg44 = (ImageView) findViewById(R.id.peg44);
        board = new ImageView[][] {{peg00}, {peg10, peg11}, {peg20, peg21, peg22}, {peg30, peg31, peg32, peg33}, {peg40, peg41, peg42, peg43, peg44}};
        text = (TextView) findViewById(R.id.textView);

        Resources res = getResources();
        emptyDraw = res.getDrawable(R.drawable.empty);
        occupiedDraw = res.getDrawable(R.drawable.occupied);
        startDraw = res.getDrawable(R.drawable.start);
        endDraw = res.getDrawable(R.drawable.end);
        
        newBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void nextMove(View view) {
        if (curMovNum < 12) {
            curMovNum ++;
            showMove();
        } else if (curMovNum == 12) {
            for (int row = 1; row < SIZE; row ++) {
                for (int col = 0; col < row+1; col ++) {
                    if (solvedBoards.elementAt(curMovNum)[row][col] == 1) {
                        board[row][col].setImageDrawable(occupiedDraw);
                    } else {
                        board[row][col].setImageDrawable(emptyDraw);
                    }
                    text.setText("Number of moves: 13\nPegs left: 1");
                }   
            }
            curMovNum = 13;
        }
    }
    
    public void prevMove(View view) {
        if (curMovNum > 0) {
            curMovNum --;
            showMove();
        } else if (curMovNum == 0) {
            board[0][0].setImageDrawable(emptyDraw);
            for (int row = 1; row < SIZE; row ++) {
                for (int col = 0; col < row+1; col ++) {
                    board[row][col].setImageDrawable(occupiedDraw);
                    text.setText("Number of moves: 0\nPegs left: 14");
                }   
            }
            curMovNum = -1;
        }
    }
    
    public void showMove() {
        int [][] curBoard = solvedBoards.elementAt(curMovNum);
        Move curMove = solvedMoves.elementAt(curMovNum);
        for (int row = 0; row < SIZE; row ++) {
            for (int col = 0; col < row+1; col ++) {
                if (curBoard[row][col] == 1) {
                    board[row][col].setImageDrawable(occupiedDraw);
                } else {
                    board[row][col].setImageDrawable(emptyDraw);
                }
            }   
        }
        int[] start = curMove.getStart();
        int[] jumped = curMove.getJumped();
        int[] end = curMove.getEnd();
        board[start[0]][start[1]].setImageDrawable(startDraw);
        board[jumped[0]][jumped[1]].setImageDrawable(emptyDraw);
        board[end[0]][end[1]].setImageDrawable(endDraw);
        text.setText("Number of moves: " + Integer.toString(curMovNum+1)+ "\nPegs left: " + Integer.toString(13-curMovNum));
    }

    public void resetBoard(View view) {
        newBoard();
    }
    
    public void newBoard() {
        board[0][0].setImageDrawable(emptyDraw);
        for (int row = 1; row < SIZE; row ++) {
            for (int col = 0; col < row+1; col ++) {
                board[row][col].setImageDrawable(occupiedDraw);
            }   
        }
        text.setText("Number of moves: 0\nPegs left: 14");
        curMovNum = -1;
        Peg myPeg = new Peg();
        myPeg.solveBoard();
        solvedMoves.clear();
        solvedBoards.clear();
        solvedMoves.addAll(myPeg.getSolvedMoves());
        solvedBoards.addAll(myPeg.getSolvedBoards());
    }
}
