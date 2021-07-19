package com.example.onlinemusicappclient.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.onlinemusicappclient.Model.Upload;
import com.example.onlinemusicappclient.R;
import com.example.onlinemusicappclient.SongsActivity;

import java.util.List;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.MyViewHoder>{


    private Context mContext;
    private List<Upload> uploads;

    public RecyclerViewAdapter(Context mContext, List<Upload> uploads) {
        this.mContext = mContext;
        this.uploads = uploads;
    }

    @NonNull

    @Override
    public MyViewHoder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.card_view_item,parent,false);

        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {

        final  Upload upload = uploads.get(position);
        holder.tv_book_title.setText(upload.getName());

        Glide.with(mContext).load(upload.getUrl()).into(holder.imd_book_thummail);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SongsActivity.class);
                intent.putExtra("songsCategory", upload.getSongsCategory());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {

        TextView tv_book_title;
        ImageView imd_book_thummail;
        CardView cardView;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_book_title = itemView.findViewById(R.id.book_title_id);
            imd_book_thummail = itemView.findViewById(R.id.book_img_id);
            cardView = itemView.findViewById(R.id.card_view_id);


        }
    }
}
