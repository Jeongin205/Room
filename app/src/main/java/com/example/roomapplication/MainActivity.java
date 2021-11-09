package com.example.roomapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Music> musicList;
    private  MusicDB musicDB = null;
    private Context mContext = null;
    private MusicAdapter musicAdapter;
    private Button mAddButton;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddButton = (Button)findViewById(R.id.mAddButton);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);

        mContext = getApplicationContext();
        musicAdapter = new MusicAdapter(musicList);

        musicDB = MusicDB.getInstance(this);

        class InsertRunnable implements Runnable{

            @Override
            public void run(){
                try{
                    musicList = MusicDB.getInstance(mContext).musicDao().getAll();
                    musicAdapter = new MusicAdapter(musicList);
                    musicAdapter.notifyDataSetChanged();

                    mRecyclerView.setAdapter(musicAdapter);
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(mLinearLayoutManager);
                }
                catch (Exception e){

                }
            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

        mAddButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        MusicDB.destroyInstance();
        musicDB = null;
    }
}