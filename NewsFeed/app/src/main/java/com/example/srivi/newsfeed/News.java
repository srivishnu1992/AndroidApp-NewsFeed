package com.example.srivi.newsfeed;

/**
 * Created by srivi on 21-02-2018.
 */

public class News {
    String title;
    String publishedAt;
    String imageUrl;
    String description;
    String link;

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
