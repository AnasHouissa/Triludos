package com.example.triludos.utils;

import static com.example.triludos.utils.Constants.*;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.triludos.HangmanActivity;
import com.example.triludos.PigDiceActivity;
import com.example.triludos.R;
import com.example.triludos.TicTacToeActivity;


public class CustomDialog {
    private Activity activity;

    public CustomDialog(Activity activity) {
        this.activity = activity;
    }


    public void showDialog(int winner, int icon, String rootActivity) {
        Dialog dialog;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(false);

        //setting image for dialog iv
        ImageView ivRes = dialog.findViewById(R.id.ivRes);
        if (icon == CUP) {
            ivRes.setImageResource(R.drawable.cup);
        } else if (icon == HAPPY) {
            ivRes.setImageResource(R.drawable.happy);
        } else if (icon == SAD) {
            ivRes.setImageResource(R.drawable.sad);
        }
        //word to guess
        if(rootActivity.equals(HANGMAN)){
            TextView tvWordToGuess = dialog.findViewById(R.id.tvWord);
            tvWordToGuess.setText(activity.getString(R.string.correct_word)+ " "+((HangmanActivity) activity).getWordToBeGuessed());
        }
        
        //setting text for dialog tv
        TextView tvWinner = dialog.findViewById(R.id.tvWinner);
        String txt;
        if(winner!=0){
            txt=activity.getString(R.string.player) + winner + activity.getString(R.string.won);
        }else{
            txt=activity.getString(R.string.draw);
        }
        tvWinner.setText(txt);
        //exit iv on click
        ImageView ivExit = dialog.findViewById(R.id.ivExit);
        ivExit.setOnClickListener(v -> {
            activity.finish();
            dialog.dismiss();
        });
        //restart iv on click
        ImageView ivRestart = dialog.findViewById(R.id.ivRestart);
        ivRestart.setOnClickListener(v -> {
            if (rootActivity.equals(PIG_DICE)) {
                ((PigDiceActivity) activity).replay();
            } else if (rootActivity.equals(TIC_TAC_TOE)) {
                ((TicTacToeActivity) activity).replay();
            } else {
                ((HangmanActivity) activity).initializeGame();
            }
            dialog.dismiss();
        });
        //show dialog
        dialog.show();
    }
}
