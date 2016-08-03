package com.sudoku.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SudokuGame extends Activity implements OnClickListener {
	   
	   
	   private static final String TAG = "SudokuGame";
	   Intent intent;

	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.main);

	      View newGameButton = findViewById(R.id.new_button);
	      newGameButton.setOnClickListener(this);
	      View aboutButton = findViewById(R.id.option_button);
	      aboutButton.setOnClickListener(this);
	      View exitButton = findViewById(R.id.exit_button);
	      exitButton.setOnClickListener(this);
		  intent = new Intent(SudokuGame.this, GameGenerator.class);
	   }

	   public void onClick(View v) {
	      switch (v.getId()) {
	      case R.id.option_button:
			 optionDialog();
	         break;

	      case R.id.new_button:
	         openNewGameDialog();
	         break;

	      case R.id.exit_button:
	         finish();
	         break;
	      }
	   }

	   //options menu
	   private void optionDialog(){
		   new AlertDialog.Builder(this)
				   .setTitle("Number")
				   .setSingleChoiceItems(R.array.hint, 0, null)
				   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int whichButton) {
						   dialog.dismiss();
						   int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
						   intent.putExtra(GameGenerator.HINTS, selectedPosition);
					   }
				   })
				   .show();
	   }

	   //user selects difficulty
	   private void openNewGameDialog() {
	      new AlertDialog.Builder(this)
	           .setTitle(R.string.new_game_title)
	           .setItems(R.array.difficulty,
	            new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialoginterface, int i) {
	                  startGame(i);
	               }
	            })
	           .show();
	   }

	   //starts game
	   private void startGame(int i) {
		      Log.d(TAG, "clicked on " + i);
		      intent.putExtra(GameGenerator.DIFFICULTY, i);
		      startActivity(intent);
		   }
}
