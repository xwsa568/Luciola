package com.KUluciola.Luciola;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Locale;

public class Activity_Videos extends AppCompatActivity {

    private ArrayList<ModelVideo> videosList = new ArrayList<ModelVideo>();
    private AdapterVideoList adapterVideoList;
    private MediaController mediaController;

    Boolean isRotate = false;
    FloatingActionButton fabMain, fabSub1, fabSub2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        initializeViews();
        checkPermissions();

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
        fabSub2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                startActivityForResult(Intent.createChooser(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("video/mp4"),"Select a video"), 1);
            }
        });
    }

    private void initializeViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView_videos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1)); //3 = column count
        adapterVideoList = new AdapterVideoList(this, videosList);
        recyclerView.setAdapter(adapterVideoList);
        this.mediaController = new MediaController(this);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else {
                loadVideos();
            }
        } else {
            loadVideos();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideos();
            } else {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadVideos() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME};
                String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

                Cursor cursor = getApplication().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);
                if (cursor != null) {
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);

                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        String title = cursor.getString(titleColumn);

                        Uri data = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                        videosList.add(new ModelVideo(id, data, title));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterVideoList.notifyItemInserted(videosList.size() - 1);
                            }
                        });
                    }
                }

            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME};

        if(requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();


             /*   if (uri != null) {
                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                            ContentValues values = new ContentValues();
                            values.put(VideoDataBase.CreateDB.TITLE, title);
                            values.put(VideoDataBase.CreateDB.URI, uri.toString());

                            //long id = cursor.getLong(cursor.getColumnIndexOrThrow(VideoDataBase.CreateDB._ID));

                            videosList.add(new ModelVideo(newRowId, uri, title));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterVideoList.notifyItemInserted(videosList.size() - 1);
                                }
                            });
                        }
                    } finally {
                        cursor.close();
                    }
                }*/
            }
        }
    }
}
