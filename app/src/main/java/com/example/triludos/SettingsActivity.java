package com.example.triludos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.triludos.databinding.ActivitySettingsBinding;
import com.example.triludos.services.BackgroundMusicService;
import com.example.triludos.utils.AppCompat;
import com.example.triludos.utils.LangManager;
import com.example.triludos.utils.TemporaryDataHolder;
import com.example.triludos.utils.TextColorSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsActivity extends AppCompat {

    private ActivitySettingsBinding b;
    private static final String TAG = "SettingsActivityDebug";

    private ImageView ivBack,ivCheckSound,ivCheckMusic,ivLangPicked,ivNoAds;
    private TextView tvSettings,tvSound,tvMusic,tvLang;

    private List<TextView> listTV=new ArrayList<>();
    private LangManager langManager;
    private SharedPreferences settingsPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b =ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        settingsPref= getSharedPreferences("SETTINGS_PREF", Context.MODE_PRIVATE);
        langManager=new LangManager(this);

        bindViews();
        initLangBox();
        musicStateInit();
        soundStateInit();
        setTextViewColor(); //creates collection of tv and set their texts' colors

        onClickListeners();
    }

    private void initLangBox() {
        if(langManager.getLang().equals("fr")){
            ivLangPicked.setImageResource(R.drawable.french_txt);
        }else{
            ivLangPicked.setImageResource(R.drawable.english_txt);
        }
    }

    private void bindViews() {
        ivBack=b.ivBack;
        tvSettings=b.tvSettings;
        tvSound=b.tvSound;
        tvMusic=b.tvMusic;
        tvLang=b.tvLang;
        ivCheckSound=b.ivCheckSound;
        ivCheckMusic=b.ivCheckMusic;
        ivLangPicked=b.ivLangPicked;
        ivNoAds=b.ivNoAds;
    }

    private void onClickListeners() {
        //back btn
        ivBack.setOnClickListener(v -> {
            finish();
        });

        //sound on off
        ivCheckSound.setOnClickListener(v -> {
            changeSoundState();
        });

        //music on off
        ivCheckMusic.setOnClickListener(v -> {
            changeMusicState();
        });

        //change language on click
        ivLangPicked.setOnClickListener(v -> {
            changeLanguageState();
        });
    }

    private void soundStateInit(){
        //empty iv
        ivCheckSound.setImageResource(0);
        //get last picked state (default on)
        int sound=settingsPref.getInt("sound",1);
        if(sound==1){
            ivCheckSound.setImageResource(R.drawable.checked);
        }else{
            ivCheckSound.setImageResource(R.drawable.unchecked);
        }
    }

    private void musicStateInit(){
        //empty iv
        ivCheckMusic.setImageResource(0);
        //get last picked state (default on)
        int music=settingsPref.getInt("music",1);
        if(music==1){
            ivCheckMusic.setImageResource(R.drawable.checked);
        }else{
            ivCheckMusic.setImageResource(R.drawable.unchecked);
        }
    }

    private void changeMusicState() {
        final SharedPreferences.Editor settingsEditor = settingsPref.edit();
        //empty iv
        ivCheckMusic.setImageResource(0);
        //get last picked state (default on)
        int music=settingsPref.getInt("music",1);
        if(music==0){
            //music on
            settingsEditor.putInt("music", 1);
            ivCheckMusic.setImageResource(R.drawable.checked);
            startMusic();
        }else{
            //music off
            settingsEditor.putInt("music", 0);
            ivCheckMusic.setImageResource(R.drawable.unchecked);
            stopMusic();
        }
        settingsEditor.apply();
    }

    private void startMusic() {
        Intent musicServ=new Intent(this, BackgroundMusicService.class);
        startService(musicServ);
        TemporaryDataHolder.getInstance().setMusicOn(true);
    }

    private void stopMusic() {
        Intent musicServ=new Intent(this, BackgroundMusicService.class);
        stopService(musicServ);
        TemporaryDataHolder.getInstance().setMusicOn(false);
    }

    private void changeLanguageState() {
        //empty iv
        ivLangPicked.setImageResource(0);
        //get last picked lang (default ENGLISH)
        if(langManager.getLang().equals("en")){
            ivLangPicked.setImageResource(R.drawable.french_txt);
            langManager.updateRes("fr");
            recreate();
        }else{
            ivLangPicked.setImageResource(R.drawable.english_txt);
            langManager.updateRes("en");
            recreate();
        }
    }

    private void changeSoundState() {
        final SharedPreferences.Editor settingsEditor = settingsPref.edit();
        //empty iv
        ivCheckSound.setImageResource(0);
        //get last picked state (default on)
        int sound=settingsPref.getInt("sound",1);
        if(sound==0){
            //sound on
            settingsEditor.putInt("sound", 1);
            ivCheckSound.setImageResource(R.drawable.checked);
        }else{
            //sound off
            settingsEditor.putInt("sound", 0);
            ivCheckSound.setImageResource(R.drawable.unchecked);
        }
        settingsEditor.apply();
    }

    private void setTextViewColor() {
        //list of text views to set text color with TextColorSetter instanciation
        Collections.addAll(listTV,tvSettings,tvSound,tvMusic,tvLang);
        TextColorSetter textColorSetter=new TextColorSetter(getApplicationContext(),listTV);
        textColorSetter.setTextColor();
    }
}