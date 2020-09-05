package com.KUluciola.Luciola;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class Activity_Player extends AppCompatActivity {

    long videoId;
    private PlayerView playerView;
    private SimpleExoPlayer player;

    static final String[] LIST_ITEM = {"보복운전",
                                        "난폭운전",
                                        "폭주레이싱",
                                        "신호위반",
                                        "끼어들기 금지위반",
                                        "통행의 금지 및 제한 위반",
                                        "교차로 통행방법위반(꼬리물기)",
                                        "제차 신호 조작 불이행(방향지시등)",
                                        "중앙선침범",
                                        "적재물 추락방지 조치위반",
                                        "적재중량, 적재용량초과",
                                        "지정차로 위반",
                                        "진로변경 위반",
                                        "교차로 통행방법 위반",
                                        "고속도록 갓길통행 위반",
                                        "기타",
                                        "이륜차위반"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initializeViews();
        videoId = getIntent().getExtras().getLong("videoId");

        ListView listview ;
        CustomChoiceListViewAdapter adapter;

        // Adapter 생성
        adapter = new CustomChoiceListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.list_item);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        for(int i = 0; i < 17; i++) {
            adapter.addItem(LIST_ITEM[i]);
        }
    }

    private void initializeViews() {
        playerView = findViewById(R.id.playerView);
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
        MediaSource mediaSource = buildMediaSource(videoUri);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, getString(R.string.app_name));
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void releasePlayer(){
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        if(Util.SDK_INT<24){
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(Util.SDK_INT>=24){
            releasePlayer();
        }
        super.onStop();
    }
}
