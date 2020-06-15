package com.zbdihd.projectnosql.service;


import com.zbdihd.projectnosql.model.Genre;
import com.zbdihd.projectnosql.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogMusicServiceImpl implements CatalogMusicService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAllGenres(){
        return genreRepository.findAll();
    }
    public void saveGenres(Genre genre){
        genreRepository.save(genre);
    }

    public Genre findGenreByName(String genreName) {
        return genreRepository.findFirstByName(genreName);
    }

}
