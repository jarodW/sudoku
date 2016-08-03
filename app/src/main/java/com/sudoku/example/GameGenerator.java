package com.sudoku.example;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class GameGenerator extends Activity {
   private static final String TAG = "Sudoku";
   public static final String DIFFICULTY = "dif";
   public static final String HINTS = "hin";

   SudokuBoard grid = SudokuBoard.getInstance();

   public int board[][] = grid.generateBoard();

   private int puzzle[][];

   public int initial[][];

   private SudokuView sudokuView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d(TAG, "onCreate");

      int diff = getIntent().getIntExtra(DIFFICULTY, 0);
      puzzle = getPuzzle(diff);
      calculateUsedValues();

      sudokuView = new SudokuView(this);
      setContentView(sudokuView);
      sudokuView.requestFocus();
   }

   //Randomly Generates a board based on the selected difficulty
   private int[][] getPuzzle(int diff) {
      int randGenPuz[][];
      switch (diff) {
      case 2:
         randGenPuz = grid.removeElements(copy(board),55);;
         break;
      case 1:
         randGenPuz = grid.removeElements(copy(board),50);;
         break;
      case 0:
      default:
         randGenPuz = grid.removeElements(copy(board),45);
         break;
      }

      Log.d(TAG, "Dif " + DIFFICULTY);
      setInitial(randGenPuz);
      return randGenPuz;
   }

   //creates a board with the inital values
   private void setInitial(int[][] arr){
      initial = new int[arr.length][arr.length];
      for(int x = 0; x < arr.length; x++){
         for(int y = 0; y < arr[x].length; y++){
            initial[x][y] = arr[x][y];
         }
      }
   }

   //get the initial board
   public int[][] getInitial(){
      return initial;
   }

   //returns the value for a specified cell
   private int getValue(int x, int y){return puzzle[x][y];}

   //sets the value for a specified cell
   private void setValue(int x, int y, int value){ puzzle[x][y] = value;}

   //returns the value of a cell as a string
   protected String getValueString(int x, int y) {
      int v = getValue(x, y);
      if (v == 0)
         return "";
      else
         return String.valueOf(v);
   }

   //set the value if it is a valid move
   protected boolean setValueIfValid(int x, int y, int value) {
      int hint = getIntent().getIntExtra(HINTS, 0);
      //if hints are set an invalid move is not allowed.
      if (hint == 1) {
         int tiles[] = getUsedValues(x, y);
         if (value != 0) {
            for (int tile : tiles) {
               if (tile == value)
                  return false;
            }
         }
         setValue(x, y, value);
         calculateUsedValues();
         return true;
      }else {
         setValue(x, y, value);
         return true;
      }
   }

   //shows the numkeys
   protected void showKeypad(int x, int y) {
      int tiles[];
      int hint = getIntent().getIntExtra(HINTS, 0);
      if( hint == 1)
         tiles = getUsedValues(x, y);
      else
         tiles = new int[0];
      if (tiles.length == 9) {
         Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
         toast.setGravity(Gravity.CENTER, 0, 0);
         toast.show();
      } else {
         Dialog v = new NumKeys(this, tiles, sudokuView);
         v.show();
      }
   }

   //keeps track of used tiles for each cell
   private final int used[][][] = new int[9][9][];

   //returns the used values for a cell
   protected int[] getUsedValues(int x, int y) {
      return used[x][y];
   }

   //calculates which values are available to be used
   private void calculateUsedValues() {
      for (int x = 0; x < 9; x++) {
         for (int y = 0; y < 9; y++) {
            used[x][y] = calculateUsedValues(x, y);
         }
      }
   }

   //calculates which values are available to be used
   private int[] calculateUsedValues(int x, int y) {
      int regLen = (int)Math.sqrt(board.length);
      int c[] = new int[board.length];
      // horizontal
      for (int i = 0; i < board.length; i++) {
         if (i == y)
            continue;
         int t = getValue(x, i);
         if (t != 0)
            c[t - 1] = t;
      }
      // vertical
      for (int i = 0; i < board.length; i++) {
         if (i == x)
            continue;
         int t = getValue(i, y);
         if (t != 0)
            c[t - 1] = t;
      }
      // same cell block
      int xPos = (x / regLen) * regLen;
      int yPos = (y / regLen) * regLen;
      for (int i = xPos; i < xPos + regLen; i++) {
         for (int j = yPos; j < yPos + regLen; j++) {
            if (i == x && j == y)
               continue;
            int t = getValue(i, j);
            if (t != 0)
               c[t - 1] = t;
         }
      }
      // compress
      int nused = 0; 
      for (int t : c) {
         if (t != 0)
            nused++;
      }
      int c1[] = new int[nused];
      nused = 0;
      for (int t : c) {
         if (t != 0)
            c1[nused++] = t;
      }
      return c1;
   }

   //checks if the puzzle board matches the completed board
   public boolean checkWinner(){
      boolean result = true;
      for(int x = 0; x < board.length; x++){
         for(int y = 0; y < board[x].length; y++){
            if(board[x][y] != puzzle[x][y])
               result = false;
         }
      }
      return result;
   }

   //produces a copy of the finished board
   public int[][] copy(int[][] board) {
      int[][] copy = new int[board.length][board.length];
      for (int x = 0; x < board.length; x++) {
         for (int y = 0; y < board.length; y++) {
            copy[x][y] = board[x][y];
         }
      }
      return copy;
   }

   //shows a nessage if the winner
   public void winner(){
      Toast toast = Toast.makeText(this, "Winner", Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.CENTER, 0, 0);
      toast.show();
   }

}
