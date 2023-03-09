package com.example.triludos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.triludos.databinding.ActivityHomeMenuBinding;
import com.example.triludos.services.BackgroundMusicService;
import com.example.triludos.utils.TemporaryDataHolder;

public class HomeMenuActivity extends AppCompatActivity {

    private ActivityHomeMenuBinding b;
    private ImageView ivSettings, ivHangman, ivPigDice, ivTicTacToe, ivInfo, ivNoAds;

    private SharedPreferences settingsPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        b = ActivityHomeMenuBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        settingsPref = getSharedPreferences("SETTINGS_PREF", Context.MODE_PRIVATE);

        bindView();
        launchers();
        startMusic();
    }

    private void startMusic() {
        int music = settingsPref.getInt("music", 1);
        if (music == 1) {
            Intent musicServ = new Intent(this, BackgroundMusicService.class);
            startService(musicServ);
        }
    }

    private void launchers() {
        //intent settings
        ivSettings.setOnClickListener(v -> {
            Intent toSettings = new Intent();
            toSettings.setClass(HomeMenuActivity.this, SettingsActivity.class);
            startActivity(toSettings);
        });

        //intent hangman
        ivHangman.setOnClickListener(v -> {
            Intent toHangman = new Intent();
            toHangman.setClass(HomeMenuActivity.this, HangmanActivity.class);
            startActivity(toHangman);
        });

        //intent pig dice
        ivPigDice.setOnClickListener(v -> {
            Intent toPigDice = new Intent();
            toPigDice.setClass(HomeMenuActivity.this, PigDiceActivity.class);
            startActivity(toPigDice);
        });

        //intent tic tac toe
        ivTicTacToe.setOnClickListener(v -> {
            Intent toTicTacToe = new Intent();
            toTicTacToe.setClass(HomeMenuActivity.this, TicTacToeActivity.class);
            startActivity(toTicTacToe);
        });
    }

    private void bindView() {
        ivSettings = b.ivSettings;
        ivHangman = b.ivHangman;
        ivPigDice = b.ivPigDice;
        ivTicTacToe = b.ivTicTacToe;
        ivInfo = b.ivInfo;
        ivNoAds = b.ivNoAds;
    }

    private boolean isMusicServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isMusicServiceRunning(BackgroundMusicService.class)) {
            stopService(new Intent(this, BackgroundMusicService.class));
        }
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