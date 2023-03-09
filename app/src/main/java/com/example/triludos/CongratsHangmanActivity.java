package com.example.triludos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.triludos.databinding.ActivityCompletedHangmanBinding;
import com.example.triludos.services.BackgroundMusicService;
import com.example.triludos.utils.TemporaryDataHolder;

public class CongratsHangmanActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityCompletedHangmanBinding b;
    private TextView tvScoreP1,tvScoreP2;
    private Button btnBack, btnReplay;
    private int p1Score=0;
    private int p2Score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityCompletedHangmanBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        p1Score=getIntent().getIntExtra("p1Score",0);
        p2Score=getIntent().getIntExtra("p2Score",0);

        bindingView();
        displayScore();

        btnBack.setOnClickListener(this);
        btnReplay.setOnClickListener(this);
    }
    private void displayScore(){
        tvScoreP1.setText(String.valueOf(p1Score));
        tvScoreP2.setText(String.valueOf(p2Score));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnReplay) {
            startActivity(new Intent(this,HangmanActivity.class));
            finish();
        }}

    private void bindingView(){
        tvScoreP1 = b.tvScoreP1;
        tvScoreP2 = b.tvScoreP2;
        btnBack = b.btnBack;
        btnReplay = b.btnReplay;

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