package com.example.srivi.newsfeed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tvShow;
    Button btnGo;
    ImageButton ibPrev;
    ImageButton ibNext;
    ImageView imageView;
    TextView tvTitle;
    TextView tvPublishedAt;
    TextView tvDescription;
    TextView tvIndex;
    TextView tvOf;
    TextView tvTotal;
    ProgressBar progressBar;
    ConnectivityManager connectivityManager;
    ArrayList<News> newsArrayList;
    GetDataAsync.IData iData;
    int index;
    int total = 0;
    CharSequence[] items = {"Top Stories", "World", "U.S.", "Business", "Politics", "Technology", "Health", "Entertainment", "Travel", "Living", "Most Recent"};
    String[] urls = {
    "http://rss.cnn.com/rss/cnn_topstories.rss",
    "http://rss.cnn.com/rss/cnn_world.rss",
    "http://rss.cnn.com/rss/cnn_us.rss",
    "http://rss.cnn.com/rss/money_latest.rss",
    "http://rss.cnn.com/rss/cnn_allpolitics.rss",
    "http://rss.cnn.com/rss/cnn_tech.rss",
    "http://rss.cnn.com/rss/cnn_health.rss",
    "http://rss.cnn.com/rss/cnn_showbiz.rss",
    "http://rss.cnn.com/rss/cnn_travel.rss",
    "http://rss.cnn.com/rss/cnn_living.rss",
    "http://rss.cnn.com/rss/cnn_latest.rss"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        tvShow = findViewById( R.id.tvShow );
        btnGo  = findViewById( R.id.btnGo );
        tvTitle = findViewById( R.id.tvTitle );
        tvPublishedAt = findViewById( R.id.tvPublishedAt );
        tvDescription = findViewById( R.id.tvDescription );
        tvIndex = findViewById( R.id.tvIndex );
        tvOf = findViewById( R.id.tvOf );
        tvTotal = findViewById( R.id.tvTotal );
        progressBar = findViewById( R.id.progressBar );
        ibNext = findViewById( R.id.ibNext );
        ibPrev = findViewById( R.id.ibPrev );
        ibNext.setEnabled( false );
        ibPrev.setEnabled( false );
        imageView = findViewById( R.id.imageView );


        ibNext.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility( View.VISIBLE );
                if(index>=total-1)
                    index=0;
                else index++;
                display();
            }
        } );

        ibPrev.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility( View.VISIBLE );
                if(index<=0)
                    index = total-1;
                else index--;
                display();
            }
        } );

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Choose Category" );
        builder.setItems( items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!isConnected())
                    Toast.makeText( MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT ).show();
                else {
                    progressBar.setVisibility( View.VISIBLE );
                    tvShow.setText( items[i] );
                    index=0;
                    new GetDataAsync( MainActivity.this ).execute( urls[i] );
                }
            }
        } );
        final AlertDialog alertDialog = builder.create();
        btnGo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        } );

    }

    public void handleData(ArrayList<News> s) {
        newsArrayList = s;
        total = newsArrayList.size();
        if(total<=1) {
            ibNext.setEnabled( false );
            ibPrev.setEnabled( false );
            if(total == 0) {
                tvTitle.setText( "" );
                tvPublishedAt.setText( "");
                tvDescription.setText( "");
                tvIndex.setText( "" );
                tvOf.setText( "" );
                tvTotal.setText( "" );
                Toast.makeText( this, "No news found", Toast.LENGTH_SHORT ).show();
                imageView.setImageResource( android.R.color.transparent );
                progressBar.setVisibility( View.INVISIBLE );
            }
            else {
                display();
            }
        }
        else {
            ibNext.setEnabled( true );
            ibPrev.setEnabled( true );
            display();
        }
    }

    public void display() {
        final News news = newsArrayList.get( index );
        tvTitle.setText( news.title );
        tvPublishedAt.setText( news.publishedAt );
        tvDescription.setText( news.description );
        tvIndex.setText( String.valueOf( index+1 ));
        tvOf.setText( "Of" );
        tvTotal.setText( String.valueOf( total ));
        Picasso.with(this).load(news.imageUrl).into(imageView);
        progressBar.setVisibility( View.INVISIBLE );
        Log.d( "Link", news.link );
        Log.d( "Desc", news.description );
        tvTitle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLink(news);
            }
        } );
        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLink(news);
            }
        } );
    }

    public void goToLink(News news) {
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(news.link) );
        startActivity( intent );
    }

    public boolean isConnected() {
        connectivityManager = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected())
            return false;
        return true;
    }
}
