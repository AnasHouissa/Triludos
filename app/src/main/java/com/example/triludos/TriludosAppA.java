package com.example.triludos;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.util.Log;

import com.example.triludos.services.BackgroundMusicService;
import com.example.triludos.utils.TemporaryDataHolder;

public class TriludosAppA extends Application {
    private String TAG = "TriludosAppADebug";


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) { // Works for Activity
            Log.d(TAG, "onTrimMemory");
            TemporaryDataHolder.getInstance().setAppWasOnBg(true);
            stopService(new Intent(this, BackgroundMusicService.class));

        }
    }

}
