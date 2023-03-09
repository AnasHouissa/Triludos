package com.example.triludos;

import static com.example.triludos.utils.Constants.*;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.triludos.databinding.ActivityPigDiceBinding;
import com.example.triludos.utils.AppCompat;
import com.example.triludos.utils.CustomDialog;
import com.example.triludos.utils.ShakeDetector;
import com.example.triludos.utils.TextColorSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class PigDiceActivity extends AppCompat {

    private ActivityPigDiceBinding b;
    private static final String TAG = "PigDiceActivityDebug";

    private ImageView ivBack,ivReplay,ivDice1,ivDice2,ivStop,ivHowToPlay,ivShake,ivPlayer1Turn,ivPlayer2Turn;
    private TextView tvPlayer1,tvPlayer1Score,tvPlayer2,tvPlayer2Score;
    private GifImageView gifDice1,gifDice2;

    private List<TextView> listTV=new ArrayList<>();
    private static final Random RANDOM = new Random();
    private boolean turnP1=true,turnP2=false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private SharedPreferences settingsPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b =ActivityPigDiceBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        settingsPref =getSharedPreferences("SETTINGS_PREF", Context.MODE_PRIVATE);
        
        bindViews();
        setTextViewColor(); //creates collection of tv and set their texts' colors
        onClickListeners();
        shakeInit();
        scoreInit();
    }

    private void scoreInit() {
        tvPlayer1Score.setText("0");
        tvPlayer2Score.setText("0");
    }

    private void shakeInit() {
        if(settingsPref.getInt("shake", 0)==0){
            ivShake.setImageResource(R.drawable.no_shake);
        }else{
            ivShake.setImageResource(R.drawable.shake);
        }
        shakeDetection();
    }

    private void bindViews() {
        ivBack=b.ivBack;
        ivReplay=b.ivReplay;
        ivStop=b.ivStop;
        ivPlayer1Turn=b.ivPlayer1Turn;
        ivPlayer2Turn=b.ivPlayer2Turn;
        ivDice1=b.ivDice1;
        ivDice2=b.ivDice2;
        gifDice1=b.gifDice1;
        gifDice2=b.gifDice2;

        ivHowToPlay=b.ivHowToPlay;
        ivShake=b.ivShake;

        tvPlayer1=b.tvPlayer1;
        tvPlayer1Score=b.tvPlayer1Score;
        tvPlayer2=b.tvPlayer2;
        tvPlayer2Score=b.tvPlayer2Score;
    }

    private void onClickListeners() {
        //back iv on click
        ivBack.setOnClickListener(v -> {
            finish();
        });
        //replay iv on click
        ivReplay.setOnClickListener(v -> {
            replay();
        });
        //how to play iv on click
        ivHowToPlay.setOnClickListener(v -> {
            Dialog dialog;
            dialog = new Dialog(PigDiceActivity.this);
            dialog.setContentView(R.layout.how_to_play_pig_dice);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            ImageView ivClose = dialog.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(v1 -> dialog.dismiss());
            //show dialog
            dialog.show();
        });
        //enable/disable shake
        ivShake.setOnClickListener(v -> {
            if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
                SharedPreferences.Editor editor = settingsPref.edit();
                if(settingsPref.getInt("shake", 0)==0){
                    editor.putInt("shake", 1);
                }else{
                    editor.putInt("shake", 0);
                }
                editor.apply();
                shakeInit();
            }else{
                Toast.makeText(this, R.string.feature_unavailable, Toast.LENGTH_LONG).show();
            }
            
        });
        //roll dices ivs on click
        ivDice1.setOnClickListener(v -> {
            rollDices();
        });
        ivDice2.setOnClickListener(v -> {
            rollDices();
        });
        //stop iv on click
        ivStop.setOnClickListener(v -> {
            stop(); //change player turn
        });
    }

    private void shakeDetection() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            // ShakeDetector initialization
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mShakeDetector = new ShakeDetector(this);
            mShakeDetector.setOnShakeListener(count -> {
                rollDices();
            });
        }
    }

    private void stop() {
        if (turnP1){
            changerPlayerTurn(PLAYER1);
        }
        else if (turnP2){
            changerPlayerTurn(PLAYER2);
        }
    }

    private void setTextViewColor() {
        //list of text views to set text color with TextColorSetter instanciation
        Collections.addAll(listTV,tvPlayer1,tvPlayer1Score,tvPlayer2,tvPlayer2Score);
        TextColorSetter textColorSetter=new TextColorSetter(getApplicationContext(),listTV);
        textColorSetter.setTextColor();
    }

    public void replay(){
        scoreInit();
        changerPlayerTurn(PLAYER2); //player 1 starts playing when restart is clicked
    }


    private void rollDices() {
        diceState(true); //roll dices
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> runOnUiThread(() -> {
            diceState(false); //stop dices roll
            int valDice1 = randomVal();
            int valDice2 = randomVal();
            int resDice1 = getResources().getIdentifier("side" + valDice1, "drawable", "com.example.triludos");
            int resDice2 = getResources().getIdentifier("side" + valDice2, "drawable", "com.example.triludos");
            ivDice1.setImageResource(resDice1);
            ivDice2.setImageResource(resDice2);
            gamePlay(valDice1,valDice2); //update score of players
        }), 2000);
    }

    private void diceState(boolean rolling){
        if(rolling){
            ivDice1.setVisibility(View.GONE);
            ivDice2.setVisibility(View.GONE);
            gifDice1.setVisibility(View.VISIBLE);
            gifDice2.setVisibility(View.VISIBLE);
        }else{
            ivDice1.setVisibility(View.VISIBLE);
            ivDice2.setVisibility(View.VISIBLE);
            gifDice1.setVisibility(View.GONE);
            gifDice2.setVisibility(View.GONE);
        }
    }

    private void gamePlay(int valDice1, int valDice2) {
        if (turnP1){
            scoreUpdate(valDice1,valDice2,PLAYER1,tvPlayer1Score);
        } else if(turnP2){
            scoreUpdate(valDice1,valDice2,PLAYER2,tvPlayer2Score);
        }
    }

    private void scoreUpdate(int valDice1, int valDice2,int currentPlayer,TextView tvPlayerScore){
        int scoreP=Integer.parseInt(tvPlayerScore.getText().toString());
        if ((valDice1!=1)&&(valDice2!=1)) {
            scoreP += valDice1 + valDice2;
            tvPlayerScore.setText(String.valueOf(scoreP));
            if (scoreP>=100){
                winnerIs(currentPlayer,CUP);
            }
        } else if ((valDice1==1)&&(valDice2==1)){
            tvPlayerScore.setText("0");
            changerPlayerTurn(currentPlayer);
        } else {
            changerPlayerTurn(currentPlayer);
        }
    }

    private void changerPlayerTurn(int currentPlayer){
        if(currentPlayer==1){
            turnP1=false;
            turnP2=true;
            //once the player palyed, change turn indicators colors
            ivPlayer1Turn.setVisibility(View.GONE);
            ivPlayer2Turn.setVisibility(View.VISIBLE);
        }else{
            turnP1=true;
            turnP2=false;
            //once the player palyed, change turn indicators colors
            ivPlayer1Turn.setVisibility(View.VISIBLE);
            ivPlayer2Turn.setVisibility(View.GONE);
        }
    }

    private void winnerIs(int playerId,int imageCte){
        //player 1 or 2 wins else if playerId=0 ==> draw
        CustomDialog customDialog=new CustomDialog(this);
        customDialog.showDialog(playerId, imageCte,PIG_DICE);
    }

    public static int randomVal() {
        return RANDOM.nextInt(6) + 1;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        mShakeDetector.setOnShakeListener(null);
        super.onPause();
    }
}