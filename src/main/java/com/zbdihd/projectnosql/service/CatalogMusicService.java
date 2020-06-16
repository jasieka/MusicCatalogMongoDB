package com.zbdihd.projectnosql.service;

import com.zbdihd.projectnosql.model.Artist;
import com.zbdihd.projectnosql.model.Genre;
import com.zbdihd.projectnosql.model.MusicLabel;

import java.time.LocalDate;
import java.util.List;

public interface CatalogMusicService {

    List<Genre> getAllGenres();
    void saveGenre(Genre genre);
    Genre findGenreByName(String genreName);
    void deleteGenreByName(String genreName);
    List<Genre> findByGenreNameStartsWithIgnoreCase(String genreName);

    List<MusicLabel> getAllMusicLabels();
    void saveMusicLabel(MusicLabel musicLabel);
    MusicLabel findMusicLabelByName(String musicLabelName);
    void deleteMusicLabelByName(String musicLabelName);
    List<MusicLabel> findByMusicLabelNameStartsWithIgnoreCase(String musicLabelName);

    List<Artist> getAllArtists();
    void saveArtist(Artist artist);
    Artist findArtistByName(String artistName);
    void deleteArtistByName(String artistName);
    List<Artist> findByArtistNameStartsWithIgnoreCase(String artistName);
    List<Artist> findCustomByRegExName(String artistName);


    List<String> getAllCountries();

    //Date pattern "dd/MM/yyyy"
    String getStringDateWithoutTime(int day, int month, int year);
    LocalDate stringToDate(String dateWithoutTime);
    String dateToString(LocalDate date);
}
