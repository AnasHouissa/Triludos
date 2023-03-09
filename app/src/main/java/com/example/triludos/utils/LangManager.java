package com.example.triludos.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LangManager {
    private Context context;
    private SharedPreferences settingsPref;

    public LangManager(Context context) {
        this.context = context;
        settingsPref = context.getSharedPreferences("SETTINGS_PREF", Context.MODE_PRIVATE);
    }

    public void updateRes(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        setLang(langCode);
    }

    public void setLang(String langCode) {
        SharedPreferences.Editor editor = settingsPref.edit();
        editor.putString("lang", langCode);
        editor.apply();
    }

    public String getLang() {
        return settingsPref.getString("lang", "en");
    }
}
