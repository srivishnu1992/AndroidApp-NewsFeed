package com.example.srivi.newsfeed;

import android.app.Activity;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by srivi on 21-02-2018.
 */

public class GetDataAsync extends AsyncTask<String, Void, ArrayList<News>> {

    MainActivity mainActivity;
    HttpURLConnection httpURLConnection = null;
    ArrayList<News> result = null;
    GetDataAsync(Activity activity) {
        this.mainActivity = (MainActivity) activity;
    }

    @Override
    protected ArrayList<News> doInBackground(String... strings) {
        result = new ArrayList<>();
        try {
            URL url = new URL( strings[0] );
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                result = NewsParser.NewsPullParser.parseNews( httpURLConnection.getInputStream() );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            if(httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<News> s) {
       mainActivity.handleData(s);
    }

    public static interface IData {
        public void handleData(ArrayList<News> s);
    }

}
