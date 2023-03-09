package com.example.triludos;

import static com.example.triludos.utils.Constants.TIC_TAC_TOE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triludos.databinding.ActivityTicTacToeBinding;
import com.example.triludos.utils.AppCompat;
import com.example.triludos.utils.Constants;
import com.example.triludos.utils.CustomDialog;
import com.example.triludos.utils.TextColorSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicTacToeActivity extends AppCompat {

    private ActivityTicTacToeBinding b;
    private static final String TAG = "TicTacToeActivityDebug";

    private ImageView ivBack,ivReplay,ivPlayer1Turn,ivPlayer2Turn;
    private TextView tvPlayer1,tvX,tvPlayer2,tvO;
    private Button[][] buttons = new Button[3][3];

    private List<TextView> listTV=new ArrayList<>();
    private int turn=1;
    private boolean turnP1,turnP2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b =ActivityTicTacToeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        bindViews(); //views binding
        setTextViewColor(); //creates collection of tv and set their texts' colors
        onClickListeners(); //method that holds all the on click listeners
    }

    private void bindViews(){
        ivBack=b.ivBack;
        ivReplay=b.ivReplay;
        ivPlayer1Turn=b.ivPlayer1Turn;
        ivPlayer2Turn=b.ivPlayer2Turn;

        tvPlayer1=b.tvPlayer1;
        tvX=b.tvX;
        tvPlayer2=b.tvPlayer2;
        tvO=b.tvO;
    }

    private void onClickListeners() {
        //back to home menu on click
        ivBack.setOnClickListener(v -> {
            finish();
        });
        //replay iv on click
        ivReplay.setOnClickListener((View.OnClickListener) v -> {
            replay();
        });

        //xo table buttons on click
        for (int i=0 ; i<3 ; i++){
            for (int j=0 ; j<3 ; j++){
                String button_id = "button"+i+j;
                int resID = getResources().getIdentifier(button_id,"id",getPackageName());
                buttons[i][j]=findViewById(resID);
                buttons[i][j].setOnClickListener(this::gamePlay);
            }
        }
    }

    private void setTextViewColor(){
        //list of text views to set text color with TextColorSetter instanciation
        Collections.addAll(listTV,tvPlayer1,tvX,tvPlayer2,tvO);
        TextColorSetter textColorSetter=new TextColorSetter(getApplicationContext(),listTV);
        textColorSetter.setTextColor();
    }

    public void replay(){
        turn=1;
        ivPlayer1Turn.setVisibility(View.VISIBLE);
        ivPlayer2Turn.setVisibility(View.GONE);
        //xo table buttons
        for (int i=0 ; i<3 ; i++){
            for (int j=0 ; j<3 ; j++){
                buttons[i][j].setText("");
            }
        }
    }


    public void gamePlay(View v){
        guessTurn();
        if (!boxIsEmpty(v)){
            Toast.makeText(this, getString(R.string.click_again), Toast.LENGTH_SHORT).show();
        }else{
            if (turnP1) {
                ((Button) v).setText("X");
                //once the player palyed, change turn indicators colors
                ivPlayer1Turn.setVisibility(View.GONE);
                ivPlayer2Turn.setVisibility(View.VISIBLE);
            }
            else if (turnP2){
                ((Button) v).setText("O");
                //once the player palyed, change turn indicators colors
                ivPlayer2Turn.setVisibility(View.GONE);
                ivPlayer1Turn.setVisibility(View.VISIBLE);
            }
            turn++;
        }
        if (gameIsOver()){
            if (turnP1){
                winnerIs(1,Constants.HAPPY);
            }
            else if (turnP2){
                winnerIs(2,Constants.HAPPY);
            }
        } else if (turn == 10) {
            winnerIs(0,Constants.SAD);
        }
    }

    private void guessTurn() {
        if (turn % 2 !=0){
            turnP1=true;
            turnP2 =false;
        } else{
            turnP2 =true;
            turnP1=false;
        }
    }

    private boolean boxIsEmpty(View v){
        return ((Button) v).getText().toString().equals("");
    }

    private boolean gameIsOver() {
        String[][] box = new String[3][3];
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                box[i][j]=buttons[i][j].getText().toString();
            }
        }
        for (int i=0;i<3;i++){
            if(!box[i][0].equals("") && box[i][0].equals(box[i][1]) && box[i][0].equals(box[i][2])){
                return true;
            }
        }
        for (int i=0;i<3;i++){
            if(!box[0][i].equals("") && box[0][i].equals(box[1][i]) && box[0][i].equals(box[2][i])){
                return true;
            }
        }
        if(!box[0][0].equals("") && box[0][0].equals(box[1][1]) && box[0][0].equals(box[2][2])){
            return true;
        }
        if(!box[0][2].equals("") && box[0][2].equals(box[1][1]) && box[0][2].equals(box[2][0])){
            return true;
        }
        return false;
    }

    private void winnerIs(int playerId,int imageCte){
        //player 1 or 2 wins else if playerId=0 ==> draw
        CustomDialog customDialog=new CustomDialog(this);
        customDialog.showDialog(playerId, imageCte,TIC_TAC_TOE);
    }
}