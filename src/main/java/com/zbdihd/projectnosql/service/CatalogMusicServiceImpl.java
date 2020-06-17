package com.zbdihd.projectnosql.service;


import com.zbdihd.projectnosql.model.Album;
import com.zbdihd.projectnosql.model.Artist;
import com.zbdihd.projectnosql.model.Genre;
import com.zbdihd.projectnosql.model.MusicLabel;
import com.zbdihd.projectnosql.repository.AlbumRepository;
import com.zbdihd.projectnosql.repository.ArtistRepository;
import com.zbdihd.projectnosql.repository.GenreRepository;
import com.zbdihd.projectnosql.repository.MusicLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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


    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> getAllArtists(){
        return artistRepository.findAll();
    }
    public void saveArtist(Artist artist){
        artistRepository.save(artist);
    }

    public Artist findArtistByName(String artistName) {
        return artistRepository.findFirstByName(artistName);
    }

    public void deleteArtistByName(String artistName){
        artistRepository.deleteByName(artistName);
    }

    public List<Artist> findByArtistNameStartsWithIgnoreCase(String artistName){
        return artistRepository.findByNameStartsWithIgnoreCase(artistName);
    }

    public List<Artist> findCustomArtistByRegExName(String artistName){
        return artistRepository.findCustomByRegExName(artistName);
    }


    @Autowired
    private AlbumRepository albumRepository;

    public List<Album> getAllAlbums(){
        return albumRepository.findAll();
    }

    public void saveAlbum(Album album){
        albumRepository.save(album);
    }

    public Album findAlbumByName(String albumName) {
        return albumRepository.findFirstByName(albumName);
    }

    public void deleteAlbumByName(String albumName){
        albumRepository.deleteByName(albumName);
    }

    public List<Album> findByAlbumNameStartsWithIgnoreCase(String albumName){
        return albumRepository.findByNameStartsWithIgnoreCase(albumName);
    }

    public List<Album> findCustomAlbumByRegExName(String albumName){
        return albumRepository.findCustomByRegExName(albumName);
    }






    public void removeMusicLabelsReferenceFromAlbums(MusicLabel musicLabel) {
        for(Album album : getAllAlbums())
        {
            if(album.getMusicLabel().equals(musicLabel))
                album.setMusicLabel(null);
        }
    }


    public void removeArtistReferenceFromAlbums(Artist artist) {
        for(Album album : getAllAlbums())
        {
            if(album.getArtist().equals(artist))
                album.setArtist(null);
        }
    }

    public void removeAlbumReferenceFromGenre(Album album) {
        for (Genre genre : getAllGenres()) {
            List<Album> listOfAlbums = new ArrayList<>();
            for (Album album1 : genre.getAlbumList()) {
                if(!album1.equals(album))
                    listOfAlbums.add(album1);
            }
            genre.setAlbumList(listOfAlbums);
        }
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
        if(dateWithoutTime.equals(""))
            return null;
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateWithoutTime, formatter);
        }
    }

    public String dateToString(LocalDate date) {
        if(date == null)
            return "";
        else
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }





}
