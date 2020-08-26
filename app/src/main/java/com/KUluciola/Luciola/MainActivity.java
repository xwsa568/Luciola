package com.KUluciola.Luciola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    Boolean isRotate = false;
    FloatingActionButton fabMain, fabSub1, fabSub2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabMain = findViewById(R.id.fabMain);
        fabSub1 = findViewById(R.id.fabSub1);
        fabSub2 = findViewById(R.id.fabSub2);

        ViewAnimation.init(fabSub1);
        ViewAnimation.init(fabSub2);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRotate = ViewAnimation.rotateFab(view, !isRotate);
                if(isRotate) {
                    ViewAnimation.showIn(fabSub1);
                    ViewAnimation.showIn(fabSub2);
                } else {
                    ViewAnimation.showOut(fabSub1);
                    ViewAnimation.showOut(fabSub2);
                }
            }
        });

        fabSub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wifiDirect = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(wifiDirect, 0);
            }
        });
    }
}