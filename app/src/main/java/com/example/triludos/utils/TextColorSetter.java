package com.example.triludos.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.widget.TextView;

import com.example.triludos.R;

import java.util.List;

public class TextColorSetter {

    private Context context;
    private List<TextView> listTV;

    public TextColorSetter(Context context,List<TextView> listTV) {
        this.context=context;
        this.listTV=listTV;
    }

    public void setTextColor(){
        for (TextView tv:listTV) {
            Bitmap bitmap
                    = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.chalkbg_txt_color);
            Shader shader = new BitmapShader(
                    bitmap,
                    Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            tv.getPaint().setShader(shader);
        }
    }
}
