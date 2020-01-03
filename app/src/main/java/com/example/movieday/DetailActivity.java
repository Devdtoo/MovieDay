package com.example.movieday;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {

    public String title, poster, release, rating, overview;
    Toolbar toolbar;

    @BindView(R.id.textViewTitle)
    TextView titleTv;
    @BindView(R.id.imageViewMovieposter)
    ImageView posterIv;
    @BindView(R.id.textView_Year)
    TextView releaseTv;
    @BindView(R.id.textView_Rate)
    TextView rateTv;
    @BindView(R.id.textView_Description)
    TextView overviewTv;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (getIntent().hasExtra("title")) {

            Bundle bundle = getIntent().getExtras();

            title = String.valueOf(bundle.get("original_title"));
            poster= String.valueOf(bundle.get("poster_path"));
            release= String.valueOf(bundle.get("release_date"));
            rating = String.valueOf(bundle.get("vote_average"));
            overview = String.valueOf(bundle.get("overview"));
        }

        titleTv.setText(title);
        releaseTv.setText(release);
        rateTv.setText(rating);
        overviewTv.setText(overview);
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+poster).error(R.drawable.error).into(posterIv);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Movie Details");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }
}
