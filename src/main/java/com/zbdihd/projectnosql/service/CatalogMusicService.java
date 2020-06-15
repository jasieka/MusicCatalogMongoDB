package com.zbdihd.projectnosql.service;

import com.zbdihd.projectnosql.model.Genre;

import java.util.List;

public interface CatalogMusicService {

    List<Genre> getAllGenres();
    void saveGenres(Genre genre);
    Genre findGenreByName(String genreName);
}
