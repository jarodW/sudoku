package com.sudoku.example;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;


public class SudokuView extends View {
    private static final String TAG = "SudokuView";
    private float width;
    private float height;
    private int posX;
    private int posY;
    private final Rect selRect = new Rect();
    private final GameGenerator gameGenerator;

    public SudokuView(Context context) {
        super(context);
        this.gameGenerator = (GameGenerator) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    //handles screen size change
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
          width = w / 9f;
          height = h / 9f;
          getRect(posX, posY, selRect);
          super.onSizeChanged(w, h, oldWidth, oldHeight);
    }

    //draw board
    protected void onDraw(Canvas canvas) {
          Paint background = new Paint();
          background.setColor(getResources().getColor(R.color.puzzle_background));
          canvas.drawRect(0, 0, getWidth(), getHeight(), background);
          Paint dark = new Paint();
          dark.setColor(getResources().getColor(R.color.dark_lines));
          Paint hilite = new Paint();
          hilite.setColor(getResources().getColor(R.color.selected_tile));
          Paint light = new Paint();
          light.setColor(getResources().getColor(R.color.light_lines));
        //draws board lines
        for (int i = 0; i < 9; i++) {
              canvas.drawLine(0, i * height, getWidth(), i * height, light);
              canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
              canvas.drawLine(i * width, 0, i * width, getHeight(), light);
              canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
        }

        for (int i = 0; i < 9; i++) {
            if (i % 3 != 0)
                continue;
            canvas.drawLine(0, i * height, getWidth(), i * height,dark);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
            canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
            canvas.drawLine(i * width + 1, 0, i * width + 1,getHeight(), hilite);
        }



        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
        foreground.setStyle(Style.FILL);
        foreground.setTextSize(height * 0.75f);
        foreground.setTextScaleX(width / height);
        foreground.setTextAlign(Paint.Align.CENTER);

        // Draw the numbers
        FontMetrics fm = foreground.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fm.ascent + fm.descent) / 2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                canvas.drawText(this.gameGenerator.getValueString(i, j), i * width + x, j * height + y, foreground);
             }
        }


        //give distinguishing color to initial tiles
        Paint clue = new Paint();
        Rect r = new Rect();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int movesleft = 9 - gameGenerator.getUsedValues(i, j).length;
                //grey cells for initial clues.
                if(gameGenerator.getInitial()[i][j] != 0) {
                    getRect(i, j, r);
                    clue.setColor(getResources().getColor(R.color.clue));
                    canvas.drawRect(r, clue);
                }
             }
        }

        //gives distinguishing color to selected tile
        Paint selected = new Paint();
        selected.setColor(getResources().getColor(R.color.selected_tile));
        canvas.drawRect(selRect, selected);
        //checks for winner
        if(gameGenerator.checkWinner()){
            gameGenerator.winner();
        }

   }

   //handles event when a user touches a cell
   public boolean onTouchEvent(MotionEvent event) {
      if (event.getAction() != MotionEvent.ACTION_DOWN)
         return super.onTouchEvent(event);

      select((int) (event.getX() / width), (int) (event.getY() / height));

      //if it is not a clue cell
      if(gameGenerator.getInitial()[posX][posY]==0)
        gameGenerator.showKeypad(posX, posY);

      return true;
   }

   //handles event when user selects a number
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      switch (keyCode) {

      
      case KeyEvent.KEYCODE_0:
      case KeyEvent.KEYCODE_SPACE: setSelectedTile(0); break;
      case KeyEvent.KEYCODE_1:     setSelectedTile(1); break;
      case KeyEvent.KEYCODE_2:     setSelectedTile(2); break;
      case KeyEvent.KEYCODE_3:     setSelectedTile(3); break;
      case KeyEvent.KEYCODE_4:     setSelectedTile(4); break;
      case KeyEvent.KEYCODE_5:     setSelectedTile(5); break;
      case KeyEvent.KEYCODE_6:     setSelectedTile(6); break;
      case KeyEvent.KEYCODE_7:     setSelectedTile(7); break;
      case KeyEvent.KEYCODE_8:     setSelectedTile(8); break;
      case KeyEvent.KEYCODE_9:     setSelectedTile(9); break;
      case KeyEvent.KEYCODE_ENTER:
      case KeyEvent.KEYCODE_DPAD_CENTER:
         gameGenerator.showKeypad(posX, posY);
         break;
         
         
      default:
         return super.onKeyDown(keyCode, event);
      }
      return true;
   }

   //set the selected value
   public void setSelectedTile(int tile) {
      if (gameGenerator.setValueIfValid(posX, posY, tile)) {
         invalidate();
      } else {
         startAnimation(AnimationUtils.loadAnimation(gameGenerator, R.anim.shake));
      }
   }

   //gets the selected rectangle
   private void select(int x, int y) {
      invalidate(selRect);
      posX = Math.min(Math.max(x, 0), 8);
      posY = Math.min(Math.max(y, 0), 8);
      getRect(posX, posY, selRect);
      invalidate(selRect);
   }

    //gets the selected rectangle
   private void getRect(int x, int y, Rect rect) {
      rect.set((int) (x * width), (int) (y * height), (int) (x * width + width), (int) (y * height + height));
   }

}
