package com.zbdihd.projectnosql.service;


import com.zbdihd.projectnosql.model.Genre;
import com.zbdihd.projectnosql.model.MusicLabel;
import com.zbdihd.projectnosql.repository.GenreRepository;
import com.zbdihd.projectnosql.repository.MusicLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Service
public class CatalogMusicServiceImpl implements CatalogMusicService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAllGenres(){
        return genreRepository.findAll();
    }
    public void saveGenre(Genre genre){
        genreRepository.save(genre);
    }

    public Genre findGenreByName(String genreName) {
        return genreRepository.findFirstByName(genreName);
    }

    public void deleteGenreByName(String genreName){
        genreRepository.deleteByName(genreName);
    }

    public List<Genre> findByGenreNameStartsWithIgnoreCase(String genreName){
        return genreRepository.findByNameStartsWithIgnoreCase(genreName);
    }

    @Autowired
    private MusicLabelRepository musicLabelRepository;

    public List<MusicLabel> getAllMusicLabels(){
        return musicLabelRepository.findAll();
    }
    public void saveMusicLabel(MusicLabel musicLabel){
        musicLabelRepository.save(musicLabel);
    }

    public MusicLabel findMusicLabelByName(String musicLabelName) {
        return musicLabelRepository.findFirstByName(musicLabelName);
    }

    public void deleteMusicLabelByName(String musicLabelName){
        musicLabelRepository.deleteByName(musicLabelName);
    }

    public List<MusicLabel> findByMusicLabelNameStartsWithIgnoreCase(String musicLabelName){
        return musicLabelRepository.findByNameStartsWithIgnoreCase(musicLabelName);
    }



    public List<String> getAllCountries()
    {
        List<String> countries = new ArrayList<String>();
        for (String countryCode : Locale.getISOCountries()) {
            Locale obj = new Locale("", countryCode);
            countries.add(obj.getDisplayCountry());
        }
        return countries;
    }

    public String getStringDateWithoutTime(int day, int month, int year)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
    }

    public LocalDate stringToDate(String dateWithoutTime) { //"dd/MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dateWithoutTime, formatter);
        return date;
    }

    public String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }





}
