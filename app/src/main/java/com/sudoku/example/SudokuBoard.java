package com.sudoku.example;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jarod on 7/25/2016.
 */
public class SudokuBoard {

    private static SudokuBoard instance;

    private ArrayList<ArrayList<Integer>> unused = new ArrayList<ArrayList<Integer>>();

    private Random rand = new Random();
    private int boardLength = 9;
    private SudokuBoard(){}

    public static SudokuBoard getInstance(){
        if( instance == null ){
            instance = new SudokuBoard();
        }
        return instance;
    }

    //generates the complete Sudoku board
    public int[][] generateBoard(){
        int[][] board = new int[boardLength][boardLength];

        int pos = 0;


        while( pos < Math.pow(boardLength,2) ){
            if( pos == 0 ){
                clearBoard(board);
            }

            if( unused.get(pos).size() != 0 ){
                int i = rand.nextInt(unused.get(pos).size());
                int number = unused.get(pos).get(i);

                if( !checkDup(board, pos , number)){
                    int xPos = pos % boardLength;
                    int yPos = pos / boardLength;

                    board[xPos][yPos] = number;

                    unused.get(pos).remove(i);

                    pos++;
                }else{
                    unused.get(pos).remove(i);
                }

            }else{
                for(int i = 1; i <= boardLength; i++ ){
                    unused.get(pos).add(i);
                }
                pos--;
            }
        }


        return board;
    }

    //removes elements from the board
    public int[][] removeElements( int[][] board, int n ){
        int i = 0;

        while( i < n ){
            int x = rand.nextInt(boardLength);
            int y = rand.nextInt(boardLength);

            if( board[x][y] != 0 ){
                board[x][y] = 0;
                i++;
            }
        }
        return board;

    }

    //clears the board
    private void clearBoard(int [][] board){
        unused.clear();

        for(int y = 0; y < boardLength; y++ ){
            for(int x = 0; x < boardLength; x++ ){
                board[x][y] = -1;
            }
        }

        for(int x = 0; x < Math.pow(boardLength,2) ; x++ ){
            unused.add(new ArrayList<Integer>());
            for(int i = 1; i <= boardLength; i++){
                unused.get(x).add(i);
            }
        }
    }

    //check for unwanted duplicates
    private boolean checkDup(int[][] board , int currentPos , final int number){
        int x = currentPos % boardLength;
        int y = currentPos / boardLength;

        if( checkHorDup(board, x, y, number) || checkVerDup(board, x, y, number) || checkRegDup(board, x, y, number) ){
            return true;
        }

        return false;
    }

    //check for duplicates horizontally
    private boolean checkHorDup(final int[][] board , final int xPos , final int yPos , final int number ){
        for( int x = xPos - 1; x >= 0 ; x-- ){
            if( number == board[x][yPos]){
                return true;
            }
        }

        return false;
    }

    //check for duplicates vertically
    private boolean checkVerDup(final int[][] board , final int xPos , final int yPos , final int number ){
        for( int y = yPos - 1; y >= 0 ; y-- ){
            if( number == board[xPos][y] ){
                return true;
            }
        }

        return false;
    }

    //check for duplicates in a region
    private boolean checkRegDup(final int[][] Sudoku , final int xPos , final int yPos , final int number ){
        int regLen = (int)Math.sqrt(boardLength);
        int xReg = xPos / regLen;
        int yReg = yPos / regLen;

        for( int x = xReg * regLen ; x < xReg * regLen + regLen ; x++ ){
            for( int y = yReg * regLen ; y < yReg * regLen + regLen ; y++ ){
                if( ( x != xPos || y != yPos ) && number == Sudoku[x][y] ){
                    return true;
                }
            }
        }

        return false;
    }

    //return the board lengths
    public int getBoardLength(){
        return boardLength;
    }

}

