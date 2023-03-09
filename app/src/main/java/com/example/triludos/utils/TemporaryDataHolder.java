package com.example.triludos.utils;

public class TemporaryDataHolder {

    private boolean appWasOnBg =false;
    private boolean musicOn=true;

    private static final TemporaryDataHolder temporaryDataHolderInstance = new TemporaryDataHolder();

    public static TemporaryDataHolder getInstance() {
        return temporaryDataHolderInstance;
    }

    public boolean isAppWasOnBg() {
        return appWasOnBg;
    }

    public void setAppWasOnBg(boolean appWasOnBg) {
        this.appWasOnBg = appWasOnBg;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }
}


