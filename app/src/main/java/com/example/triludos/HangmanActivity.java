package com.example.triludos;

import static com.example.triludos.utils.Constants.HANGMAN;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triludos.data.WordDAO;
import com.example.triludos.data.WordEntity;
import com.example.triludos.data.WordsDatabase;
import com.example.triludos.databinding.ActivityHangmanBinding;
import com.example.triludos.services.BackgroundMusicService;
import com.example.triludos.utils.AppCompat;
import com.example.triludos.utils.Constants;
import com.example.triludos.utils.CustomDialog;
import com.example.triludos.utils.LangManager;
import com.example.triludos.utils.TemporaryDataHolder;
import com.example.triludos.utils.TextColorSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HangmanActivity extends AppCompat implements View.OnClickListener {

    private static final String TAG = "HangmanActivityDebug";
    private ActivityHangmanBinding b;

    private ConstraintLayout mainLayout, splashLayout;
    private TextView tvWordToGuess, tvPlayer1, tvPlayer2, tvScorePl2, tvScorePl1;
    private Button btnHint, btnHowToPlay, btnBack, btnReplay,
            btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI,
            btnJ, btnK, btnL, btnM, btnN, btnO, btnP, btnQ, btnR,
            btnS, btnT, btnU, btnV, btnW, btnX, btnY, btnZ;
    private List<Button> btnsAZ;

    private ImageView ivHangmanLeft, ivHangmanRight, ivPlayer1Turn, ivPlayer2Turn;
    private ArrayList<String> listOfWordsToBeGuessedWithHints;
    private ArrayList<WordEntity> listOfWordsFromDB;
    private String wordToBeGuessedWithHint;
    private String wordToBeGuessed;
    private String hintOfWordToBeGuessed;
    private char[] wordDisplayedCharArray;
    private String wordDisplayed;

    private int turn;

    private int errorsPlayer1;
    private int errorsPlayer2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private CustomDialog customDialog;

    private WordDAO db;
    private LangManager langManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityHangmanBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


        db = WordsDatabase.getInstance(this).wordDAO();
        customDialog = new CustomDialog(this);
        listOfWordsToBeGuessedWithHints = new ArrayList<>();
        listOfWordsFromDB = new ArrayList<>();
        btnsAZ = new ArrayList<>();
        langManager = new LangManager(this);

        bindViews();
        setTextColorFromPattern();

        Collections.addAll(btnsAZ, btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI,
                btnJ, btnK, btnL, btnM, btnN, btnO, btnP, btnQ, btnR,
                btnS, btnT, btnU, btnV, btnW, btnX, btnY, btnZ);

        btnBack.setOnClickListener(this);
        btnReplay.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnHowToPlay.setOnClickListener(this);
        setLettersClickListeners(btnsAZ);

        new getData().execute();

    }

    private class getData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (langManager.getLang().equals("en")) {
                listOfWordsFromDB = (ArrayList<WordEntity>) db.getEnglishWords();
            } else {
                listOfWordsFromDB = (ArrayList<WordEntity>) db.getFrenchWords();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            splashLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            turn = 2;
            fillListWithWords(listOfWordsFromDB);
            initializeGame();
        }
    }

    public void initializeGame() {

        for (Button btn : btnsAZ) {
            btn.setBackground(ContextCompat.getDrawable(this, R.drawable.chalk_frame));

        }
        ivHangmanLeft.setImageResource(R.drawable.gallow);
        ivHangmanRight.setImageResource(R.drawable.gallow);
        errorsPlayer1 = 0;
        errorsPlayer2 = 0;

        if (listOfWordsToBeGuessedWithHints.isEmpty()) {
            Intent intent = new Intent(this, CongratsHangmanActivity.class);
            intent.putExtra("p1Score", scoreP1);
            intent.putExtra("p2Score", scoreP2);
            startActivity(intent);
            finish();
        } else {

            Collections.shuffle(listOfWordsToBeGuessedWithHints);
            wordToBeGuessedWithHint = listOfWordsToBeGuessedWithHints.get(0);
            listOfWordsToBeGuessedWithHints.remove(0);
            wordToBeGuessed = wordToBeGuessedWithHint.split(":")[0];
            hintOfWordToBeGuessed = wordToBeGuessedWithHint.split(":")[1];
            //.replace(",", " ");
            wordDisplayedCharArray = wordToBeGuessed.toCharArray();
            for (int i = 1; i < wordDisplayedCharArray.length - 1; i++) {
                wordDisplayedCharArray[i] = '_';
            }
            revealLetterInWord(wordDisplayedCharArray[0]);
            revealLetterInWord(wordDisplayedCharArray[wordDisplayedCharArray.length - 1]);
            wordDisplayed = String.valueOf(wordDisplayedCharArray);
            displayWord();

        }

    }

    private void fillListWithWords(List<WordEntity> wordsList) {
        for (WordEntity w : wordsList) {
            listOfWordsToBeGuessedWithHints.add(w.getWord() + ":" + w.getHint());
        }
    }

    private void revealLetterInWord(char letter) {
        int indexletter = wordToBeGuessed.indexOf(letter);

        while (indexletter >= 0) {
            wordDisplayedCharArray[indexletter] = wordToBeGuessed.charAt(indexletter);
            indexletter = wordToBeGuessed.indexOf(letter, indexletter + 1);
        }
        wordDisplayed = String.valueOf(wordDisplayedCharArray);
    }

    private void alreadyUsedLetters() {
        for (Button btn : btnsAZ) {
            String[] wordToguessDisplayedLetters = tvWordToGuess.getText().toString().split("");
            for (String str : wordToguessDisplayedLetters) {
                if (btn.getText().toString().toUpperCase().equals(str)) {

                    btn.setBackground(ContextCompat.getDrawable(this, R.drawable.no_letter));
                }
            }

        }
    }

    private void displayWord() {
        String formattedString = "";
        for (char character : wordDisplayedCharArray) {
            formattedString += character + " ";
        }
        tvWordToGuess.setText(formattedString);
        alreadyUsedLetters();

    }

    private void checkIfLetterInWord(char letter) {

        if (wordToBeGuessed.indexOf(letter) >= 0) {
            alreadyUsedLetters();
            if (wordDisplayed.indexOf(letter) < 0) {
                revealLetterInWord(letter);
                displayWord();
                if (turn % 2 == 0) {
                    {
                        if (!wordDisplayed.contains("_")) {
                            customDialog.showDialog(1, Constants.CUP, HANGMAN);
                            scoreP1 += 3;
                        } else {
                            scoreP1 += 1;
                        }
                        tvScorePl1.setText(String.valueOf(scoreP1));

                    }
                } else {
                    {
                        if (!wordDisplayed.contains("_")) {
                            customDialog.showDialog(2, Constants.CUP, HANGMAN);
                            scoreP2 += 3;
                        } else {
                            scoreP2 += 1;
                        }
                        tvScorePl2.setText(String.valueOf(scoreP2));

                    }
                }

            }
        } else {
            if (turn % 2 == 0) {
                errorsPlayer1 += 1;
                switch (errorsPlayer1) {
                    case 1:
                        ivHangmanLeft.setImageResource(R.drawable.head);
                        ivPlayer1Turn.setVisibility(View.GONE);
                        ivPlayer2Turn.setVisibility(View.VISIBLE);
                        turn += 1;
                        break;
                    case 2:
                        ivHangmanLeft.setImageResource(R.drawable.body);
                        ivPlayer1Turn.setVisibility(View.GONE);
                        ivPlayer2Turn.setVisibility(View.VISIBLE);
                        turn += 1;
                        break;
                    case 3:
                        ivHangmanLeft.setImageResource(R.drawable.hand1);
                        ivPlayer1Turn.setVisibility(View.GONE);
                        ivPlayer2Turn.setVisibility(View.VISIBLE);
                        turn += 1;
                        break;
                    case 4:
                        ivHangmanLeft.setImageResource(R.drawable.hand2);
                        ivPlayer1Turn.setVisibility(View.GONE);
                        ivPlayer2Turn.setVisibility(View.VISIBLE);
                        turn += 1;
                        break;
                    case 5:
                        ivHangmanLeft.setImageResource(R.drawable.leg1);
                        ivPlayer1Turn.setVisibility(View.GONE);
                        ivPlayer2Turn.setVisibility(View.VISIBLE);
                        turn += 1;
                        break;
                    case 6:
                        ivHangmanLeft.setImageResource(R.drawable.leg2);
                        ivPlayer1Turn.setVisibility(View.GONE);
                        ivPlayer2Turn.setVisibility(View.VISIBLE);
                        customDialog.showDialog(2, Constants.CUP, HANGMAN);
                        turn += 1;
                        break;
                }

            } else {
                errorsPlayer2 += 1;
                switch (errorsPlayer2) {
                    case 1:
                        ivHangmanRight.setImageResource(R.drawable.head);
                        ivPlayer1Turn.setVisibility(View.VISIBLE);
                        ivPlayer2Turn.setVisibility(View.GONE);
                        turn += 1;
                        break;
                    case 2:
                        ivHangmanRight.setImageResource(R.drawable.body);
                        ivPlayer1Turn.setVisibility(View.VISIBLE);
                        ivPlayer2Turn.setVisibility(View.GONE);
                        turn += 1;
                        break;
                    case 3:
                        ivHangmanRight.setImageResource(R.drawable.hand1);
                        ivPlayer1Turn.setVisibility(View.VISIBLE);
                        ivPlayer2Turn.setVisibility(View.GONE);
                        turn += 1;
                        break;
                    case 4:
                        ivHangmanRight.setImageResource(R.drawable.hand2);
                        ivPlayer1Turn.setVisibility(View.VISIBLE);
                        ivPlayer2Turn.setVisibility(View.GONE);
                        turn += 1;
                        break;
                    case 5:
                        ivHangmanRight.setImageResource(R.drawable.leg1);
                        ivPlayer1Turn.setVisibility(View.VISIBLE);
                        ivPlayer2Turn.setVisibility(View.GONE);
                        turn += 1;
                        break;
                    case 6:
                        ivHangmanRight.setImageResource(R.drawable.leg2);
                        ivPlayer1Turn.setVisibility(View.VISIBLE);
                        ivPlayer2Turn.setVisibility(View.GONE);
                        customDialog.showDialog(1, Constants.CUP, HANGMAN);
                        turn += 1;
                        break;
                }

            }
        }

    }

    private void bindViews() {
        mainLayout = b.mainLayout;
        splashLayout = b.splashLayout;

        tvWordToGuess = b.tvWordToGuess;
        tvPlayer1 = b.tvPlayer1;
        tvPlayer2 = b.tvPlayer2;
        tvScorePl2 = b.tvScorePl2;
        tvScorePl1 = b.tvScorePl1;

        ivHangmanLeft = b.ivHangmanLeft;
        ivHangmanRight = b.ivHangmanRight;
        ivPlayer1Turn = b.ivPlayer1Turn;
        ivPlayer2Turn = b.ivPlayer2Turn;

        btnHint = b.btnHint;
        btnHowToPlay = b.btnHowToPlay;
        btnBack = b.btnBack;
        btnReplay = b.btnReplay;

        btnA = b.btnA;
        btnB = b.btnB;
        btnC = b.btnC;
        btnD = b.btnD;
        btnE = b.btnE;
        btnF = b.btnF;
        btnG = b.btnG;
        btnH = b.btnH;
        btnI = b.btnI;
        btnJ = b.btnJ;
        btnK = b.btnK;
        btnL = b.btnL;
        btnM = b.btnM;
        btnN = b.btnN;
        btnO = b.btnO;
        btnP = b.btnP;
        btnQ = b.btnQ;
        btnR = b.btnR;
        btnS = b.btnS;
        btnT = b.btnT;
        btnU = b.btnU;
        btnV = b.btnV;
        btnW = b.btnW;
        btnX = b.btnX;
        btnY = b.btnY;
        btnZ = b.btnZ;

    }

    private void setLettersClickListeners(List<Button> btnsAZ) {
        for (Button btn : btnsAZ) {
            btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnReplay) {
            initializeGame();
        } else if (id == R.id.btnHint) {
            View layout = getLayoutInflater().inflate(R.layout.custom_hint_toast,
                    (ViewGroup) findViewById(R.id.toastParent));
            TextView text = (TextView) layout.findViewById(R.id.tvToastMsg);
            text.setText(hintOfWordToBeGuessed);
            Toast toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        } else if (id == R.id.btnHowToPlay) {
            Dialog dialog;
            dialog = new Dialog(HangmanActivity.this);
            dialog.setContentView(R.layout.how_to_play_hangman);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            ImageView ivClose = dialog.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(v1 -> dialog.dismiss());
            //show dialog
            dialog.show();
        } else if (id == R.id.btnA) {
            checkIfLetterInWord(btnA.getText().toString().toUpperCase().charAt(0));
        } else if (id == R.id.btnB) {
            checkIfLetterInWord(btnB.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnC) {
            checkIfLetterInWord(btnC.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnD) {
            checkIfLetterInWord(btnD.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnE) {
            checkIfLetterInWord(btnE.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnF) {
            checkIfLetterInWord(btnF.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnG) {
            checkIfLetterInWord(btnG.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnH) {
            checkIfLetterInWord(btnH.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnI) {
            checkIfLetterInWord(btnI.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnJ) {
            checkIfLetterInWord(btnJ.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnK) {
            checkIfLetterInWord(btnK.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnL) {
            checkIfLetterInWord(btnL.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnM) {
            checkIfLetterInWord(btnM.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnN) {
            checkIfLetterInWord(btnN.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnO) {
            checkIfLetterInWord(btnO.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnP) {
            checkIfLetterInWord(btnP.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnQ) {
            checkIfLetterInWord(btnQ.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnR) {
            checkIfLetterInWord(btnR.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnS) {
            checkIfLetterInWord(btnS.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnT) {
            checkIfLetterInWord(btnT.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnU) {
            checkIfLetterInWord(btnU.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnV) {
            checkIfLetterInWord(btnV.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnW) {
            checkIfLetterInWord(btnW.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnX) {
            checkIfLetterInWord(btnX.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnY) {
            checkIfLetterInWord(btnY.getText().toString().toUpperCase().charAt(0));

        } else if (id == R.id.btnZ) {
            checkIfLetterInWord(btnZ.getText().toString().toUpperCase().charAt(0));

        }

    }

    private void setTextColorFromPattern() {
        List<TextView> listTV = new ArrayList<>();
        Collections.addAll(listTV, tvWordToGuess, tvPlayer1, tvPlayer2);
        TextColorSetter textColorSetter = new TextColorSetter(getApplicationContext(), listTV);
        textColorSetter.setTextColor();
    }

    public String getWordToBeGuessed() {
        return wordToBeGuessed;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TemporaryDataHolder.getInstance().isAppWasOnBg()){
            if(TemporaryDataHolder.getInstance().isMusicOn()){
                startService(new Intent(this, BackgroundMusicService.class));
            }

        }

    }
}