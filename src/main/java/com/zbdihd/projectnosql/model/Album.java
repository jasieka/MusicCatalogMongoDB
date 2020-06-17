package com.zbdihd.projectnosql.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Album")
public class Album {
    @Id
    private String id;

    private String name;
    @DBRef
    private Artist artist;
    private List<String> tracks;

    private Map<String, Integer> ratings; // "userName;rating"
    private double averageRating; //calculated after each operation on ratings

    private String imageURL;

    @DBRef
    private MusicLabel musicLabel;
    private int releaseYear;
    private int numberOfCDs;
    private String description;

    private Date lastModifiedAt; //or created


    public Album(String name, Artist artist, List<String> tracks, Map<String, Integer> ratings, double averageRating, String imageURL, MusicLabel musicLabel, int releaseYear, int numberOfCDs, String description, Date lastModifiedAt) {
        this.name = name;
        this.artist = artist;
        this.tracks = tracks;
        this.ratings = ratings;
        this.averageRating = averageRating;
        this.imageURL = imageURL;
        this.musicLabel = musicLabel;
        this.releaseYear = releaseYear;
        this.numberOfCDs = numberOfCDs;
        this.description = description;
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getArtistName(){
        if(artist == null)
            return "";
        return artist.getName();
    }

    public String getMusicLabelName(){
        if(musicLabel == null)
            return "";
        return musicLabel.getName();
    }

    public void refreshAverageRating(){
        if(ratings.size() > 0)
            averageRating = ratings.values().stream().mapToInt(i -> i).average().orElse(0);
    }
}
