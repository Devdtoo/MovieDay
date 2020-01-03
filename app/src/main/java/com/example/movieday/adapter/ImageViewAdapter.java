package com.example.movieday.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieday.DetailActivity;
import com.example.movieday.MovieDetails;
import com.example.movieday.R;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.MyViewHolder> {

    ArrayList<MovieDetails> dataList;
    Context c;

    public ImageViewAdapter( ArrayList<MovieDetails> dataList, Context c) {
        this.dataList = dataList;
        this.c = c;
    }

    @NonNull
    @Override
    public ImageViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(c).inflate(R.layout.image_layout,parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewAdapter.MyViewHolder holder, final int position) {
        Picasso.Builder builder = new Picasso.Builder(c).memoryCache(new LruCache(24000));
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Toast.makeText(c, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Picasso pic = builder.build();
//        pic.load("https://image.tmdb.org/t/p/w185" + dataList.get(position).getPosterPath()).into(holder.imageView);
        pic.load("https://image.tmdb.org/t/p/w500"+dataList.get(position).getPosterPath()).error(R.drawable.error).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, DetailActivity.class);
                intent.putExtra("original_title", dataList.get(position).getTitle());
                intent.putExtra("poster_path", dataList.get(position).getPosterPath());
                intent.putExtra("release_date", dataList.get(position).getReleaseDate());
                intent.putExtra("vote_average", dataList.get(position).getVoteAverage());
                intent.putExtra("overview", dataList.get(position).getOverview());
                c.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
