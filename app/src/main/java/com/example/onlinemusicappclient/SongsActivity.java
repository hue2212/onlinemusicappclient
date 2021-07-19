package com.example.onlinemusicappclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.onlinemusicappclient.Adapter.JcSongAdapter;
import com.example.onlinemusicappclient.Model.GetSongs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.List;

public class SongsActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    ProgressBar progressBar;
    Boolean ckeckin = false;
    List<GetSongs> mupload;
    JcSongAdapter adapter;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private  int currentIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        recyclerView = findViewById(R.id.recycleview);
        progressBar = findViewById(R.id.progressbarshowsong);
        jcPlayerView = findViewById(R.id.jcplayer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mupload = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        adapter = new JcSongAdapter(getApplicationContext(), mupload, new JcSongAdapter.RecycleItemClickListener() {
            @Override
            public void onClickListenner(GetSongs songs, int postion) {

                changeSelectedSong(postion);

                jcPlayerView.playAudio(jcAudios.get(postion));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mupload.clear();
                for (DataSnapshot dss: datasnapshot.getChildren()){
                    GetSongs getSongs = dss.getValue(GetSongs.class);
                    getSongs.setMkey(dss.getKey());
                    currentIndex = 0;
                    final String s = getIntent().getExtras().getString("songsCategory");
                    if(s.equals(getSongs.getSongsCategory())){
                        mupload.add(getSongs);
                        ckeckin = true;
                        jcAudios.add(JcAudio.createFromURL(getSongs.getSongTitle(),getSongs.getSongLink()));
                    }


                }

                adapter.setSelectedPosition(0);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if(ckeckin){
                    jcPlayerView.initPlaylist(jcAudios,null);
                }else{
                    Toast.makeText(SongsActivity.this,"there is no songs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                progressBar.setVisibility(View.GONE);
            }
        });
    }
    public void changeSelectedSong (int index){

        adapter.notifyItemChanged(adapter.getSelectedPosition());
        currentIndex = index;
        adapter.setSelectedPosition(currentIndex);
        adapter.notifyItemChanged(currentIndex);

    }
}