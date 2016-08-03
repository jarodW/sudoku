package com.sudoku.example;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
public class NumKeys extends Dialog {
    protected static final String TAG = "NumKeys";
    private final View keys[] = new View[10];
    private View keypad;
    private final int usedKeys[];
    private final SudokuView sudokuView;

    public NumKeys(Context context, int used[], SudokuView sudokuView){
        super(context);
        this.usedKeys = used;
        this.sudokuView = sudokuView;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numkeys);
        loadViews();
        for (int element : usedKeys){
            if (element != 0)
        keys[element - 1].setVisibility(View.INVISIBLE);
        }
        setListeners();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int value = 0;
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE: value = 0; break;
            case KeyEvent.KEYCODE_1:     value = 1; break;
            case KeyEvent.KEYCODE_2:     value = 2; break;
            case KeyEvent.KEYCODE_3:     value = 3; break;
            case KeyEvent.KEYCODE_4:     value = 4; break;
            case KeyEvent.KEYCODE_5:     value = 5; break;
            case KeyEvent.KEYCODE_6:     value = 6; break;
            case KeyEvent.KEYCODE_7:     value = 7; break;
            case KeyEvent.KEYCODE_8:     value = 8; break;
            case KeyEvent.KEYCODE_9:     value = 9; break;
            default:
            return super.onKeyDown(keyCode, event);
        }

        if (isValid(value)) {
            returnResult(value);
        }
        return true;
    }


    private void returnResult(int value) {
        sudokuView.setSelectedTile(value);
        dismiss();
    }

    private boolean isValid(int value){
        for (int t : usedKeys){
            if (value == t)
            return false;
        }
        return true;
    }


    private void loadViews() {
        keypad = findViewById(R.id.numKey);
        keys[0] = findViewById(R.id.numKey_1);
        keys[1] = findViewById(R.id.numKey_2);
        keys[2] = findViewById(R.id.numKey_3);
        keys[3] = findViewById(R.id.numKey_4);
        keys[4] = findViewById(R.id.numKey_5);
        keys[5] = findViewById(R.id.numKey_6);
        keys[6] = findViewById(R.id.numKey_7);
        keys[7] = findViewById(R.id.numKey_8);
        keys[8] = findViewById(R.id.numKey_9);
        keys[9] = findViewById(R.id.numKey_0);
    }


    private void setListeners() {
        for (int i = 0; i < keys.length-1; i++) {
            final int t = i + 1;
            keys[i].setOnClickListener(new View.OnClickListener() {public void onClick(View v) {returnResult(t);}});
        }
        keys[9].setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                returnResult(0);
            }
        });
        keypad.setOnClickListener(new View.OnClickListener()
    {
    public void onClick(View v)
    {
    returnResult(0);
    }
    });
    }
}

