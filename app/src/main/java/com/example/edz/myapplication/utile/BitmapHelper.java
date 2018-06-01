package com.example.edz.myapplication.utile;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by EDZ on 2018/3/28.
 */

public class BitmapHelper {

    public static void displayImage(Activity context,ImageView imageView, String url) {
        try {
            Glide.with(context).load(url).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
