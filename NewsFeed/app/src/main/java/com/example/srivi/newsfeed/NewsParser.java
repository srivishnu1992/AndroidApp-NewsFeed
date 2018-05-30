package com.example.srivi.newsfeed;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by srivi on 21-02-2018.
 */

public class NewsParser {
    public static class NewsPullParser {
        static public ArrayList<News> parseNews(InputStream inputStream) throws XmlPullParserException, IOException {
            ArrayList<News> newsArrayList = new ArrayList<>(  );
            News news = null;
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput( inputStream, "UTF-8" );
            int event = xmlPullParser.getEventType();
            boolean media = false;
            while (event != xmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("item")) {
                            news = new News();
                            media = false;
                        }
                        if(news!=null) {
                            if(xmlPullParser.getName().equals( "title" ) )
                                news.title = xmlPullParser.nextText();
                            else if(xmlPullParser.getName().equals( "pubDate" ))
                                news.publishedAt = xmlPullParser.nextText();
                            else if (xmlPullParser.getName().equals( "description" ))
                                news.description = parse(xmlPullParser.nextText());
                            else if (xmlPullParser.getName().equals( "link" ))
                                news.link = xmlPullParser.nextText();
                            else if(xmlPullParser.getName().equals( "media:content" ) && media == false) {
                                news.imageUrl = xmlPullParser.getAttributeValue( 1 );
                                media = true;
                            }
                            else if(xmlPullParser.getName().equals( "media:thumbnail" )) {
                                news.imageUrl = xmlPullParser.getAttributeValue( 0 );
                                Log.d("url", news.imageUrl);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlPullParser.getName().equals("item")) {
                            newsArrayList.add( news );
                            news = null;
                        }
                        break;
                    default:
                        break;
                }
                event = xmlPullParser.next();
            }
            return newsArrayList;
        }
        static public String parse(String s) {
            if(s == null || s.equals( "" ))
                return s;
            StringBuilder sb = new StringBuilder( s );
            int i;
            for (i=0;i<sb.length();i++)
                if(sb.charAt( i )=='<')
                    break;
            return sb.toString().substring( 0,i );
        }
    }
}
