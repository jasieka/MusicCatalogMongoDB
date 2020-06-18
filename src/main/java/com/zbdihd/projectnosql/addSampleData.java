package com.zbdihd.projectnosql;


import com.zbdihd.projectnosql.model.*;
import com.zbdihd.projectnosql.repository.*;
import com.zbdihd.projectnosql.service.CatalogMusicService;
import com.zbdihd.projectnosql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class addSampleData implements CommandLineRunner {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MusicLabelRepository musicLabelRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CatalogMusicService catalogMusicService;


    @Override
    public void run(String... args) throws Exception {
        deleteAll();
        addData();
        displayData();
    }

    public void addData() {
        addGenres();
        addMusicLabels();
        addArtists(); //Artist/Band
        addAlbums();
        addUsers();
    }

    public void addUsers(){
        userService.addRole("ADMIN");
        userService.addRole("USER");

        userService.addUser("user", "user", "user");
        userService.addUser("user1", "user1", "user");
        userService.addUser("admin", "admin", "admin");

        //userService.addRoleToUser("user", "admin");

        //userService.changeStatusEnabled(userService.getUser("admin"));

    }


    public void addGenres(){
        genreRepository.save(new Genre("Jazz", new ArrayList<Album>()));
        genreRepository.save(new Genre("Classical", new ArrayList<Album>()));
        genreRepository.save(new Genre("Pop", new ArrayList<Album>()));
        genreRepository.save(new Genre("Rock", new ArrayList<Album>()));
        genreRepository.save(new Genre("Soundtracks", new ArrayList<Album>()));
        genreRepository.save(new Genre("Rap & Hip-Hop", new ArrayList<Album>()));
    }

    public void addMusicLabels(){
        musicLabelRepository.save(new MusicLabel("E-music", catalogMusicService.getStringDateWithoutTime(2, Calendar.FEBRUARY, 2014),
                "Poland", "Jacek Kowalczyk", new Date()));
        musicLabelRepository.save(new MusicLabel("Ultra Records", catalogMusicService.getStringDateWithoutTime(28, Calendar.MAY, 2007),
                "United States", "Patrick Moxey", new Date()));
        musicLabelRepository.save(new MusicLabel("Warner Music Group", catalogMusicService.getStringDateWithoutTime(15, Calendar.JULY, 1958),
                "United States", "Dannii Minogue", new Date()));
        musicLabelRepository.save(new MusicLabel("K Records", catalogMusicService.getStringDateWithoutTime(25, Calendar.FEBRUARY, 1975),
                "Canada", "Calvin Johnson", new Date()));
        musicLabelRepository.save(new MusicLabel("Chronological Classics", catalogMusicService.getStringDateWithoutTime(10, Calendar.DECEMBER, 1989),
                "France", "Gilles Pétard", new Date()));
        musicLabelRepository.save(new MusicLabel("Sony Music", catalogMusicService.getStringDateWithoutTime(6, Calendar.MAY, 1990),
                "United States", "Ton Bryan", new Date()));
    }

    public void addArtists(){
        artistRepository.save(new Artist("Michael Jackson", "United States",
                catalogMusicService.getStringDateWithoutTime(29, Calendar.AUGUST, 1958), catalogMusicService.getStringDateWithoutTime(25, Calendar.JUNE, 2009), new Date()));
        artistRepository.save(new Artist("Hans Zimmer", "Germany",
                catalogMusicService.getStringDateWithoutTime(12, Calendar.SEPTEMBER, 1957), "", new Date()));
        artistRepository.save(new Artist("Amy Winehouse", "United States",
                catalogMusicService.getStringDateWithoutTime(14, Calendar.SEPTEMBER, 1983), catalogMusicService.getStringDateWithoutTime(23, Calendar.JULY, 2011), new Date()));
        artistRepository.save(new Artist("Michał Lorenc", "Poland",
                catalogMusicService.getStringDateWithoutTime(5, Calendar.OCTOBER, 1955), "", new Date()));
        artistRepository.save(new Artist("Frank Sinatra", "United States",
                catalogMusicService.getStringDateWithoutTime(12, Calendar.DECEMBER, 1915), catalogMusicService.getStringDateWithoutTime(14, Calendar.MAY, 1998), new Date()));
        artistRepository.save(new Artist("Bryan Adams", "Canada",
                catalogMusicService.getStringDateWithoutTime(12, Calendar.NOVEMBER, 1959), "", new Date()));
        artistRepository.save(new Artist("Sylwia Grzeszczak", "Poland",
                catalogMusicService.getStringDateWithoutTime(7, Calendar.APRIL, 1989), "", new Date()));
    }


    public void addAlbums(){

        //
        //Hans Zimmer - Interstellar
        //
        List<String> tracks = Arrays.asList("Dreaming Of The Crash",
                "Cornfield Chase",
                "Dust",
                "Day One",
                "Message from Home",
                "The Wormhole",
                "Afraid of Time",
                "A Place Among the Stars",
                "Running Out",
                "I'm Going Home",
                "Coward",
                "Detach",
                "S.T.A.Y.",
                "Where We're Going");
        albumRepository.save(new Album("Interstellar",
                artistRepository.findFirstByName("Hans Zimmer"),
                tracks,
                new HashMap<String, Integer>(),
                0,
                "https://images-na.ssl-images-amazon.com/images/I/41VzuDJLCXL.jpg",
                musicLabelRepository.findFirstByName("Sony Music"),
                2014,
                1,
                "Interstellar pairs the creative forces of Hans Zimmer and esteemed director Christopher Nolan, who collaborated previously on The Dark Knight film trilogy and Inception. Chris wanted us to push the limits, offers Zimmer. Every conversation was about pushing boundaries and exploring new territories. This movie virtually dictates that you put everything on the line and keep the laboratory doors wide open and experiment to the very end. It tested our limits: the limits of what musicians are capable of, the limits of what could be recorded, the limits of everyone's stamina, commitment and invention, and I think we got it.\n" +
                        "\n" +
                        "I believe that Hans score for Interstellar has the tightest bond between music and image that we've yet achieved, Nolan reflected. And we're excited for people to be able to revisit the soundtrack once they ve had the chance to experience the music in the film itself.",
                new Date()
        ));
        //Add album to genre
        Genre genreSoundtracks = genreRepository.findFirstByName("Soundtracks");
        List<Album> genreSoundtracksListOfAlbum = genreSoundtracks.getAlbumList();
        genreSoundtracksListOfAlbum.add(albumRepository.findFirstByName("Interstellar"));
        genreSoundtracks.setAlbumList(genreSoundtracksListOfAlbum);
        genreRepository.save(genreSoundtracks);

        //
        //Hans Zimmer - Live In Prague
        //

        tracks = Arrays.asList("Medley: Driving (Driving Miss Daisy) / Discombobulate (Sherlock Holmes) / Zoosters Breakout (Madagascar)",
                "Medley: Crimson Tide / 160 BPM (Angels And Demons)",
                "Gladiator Medley: The Wheat / The Battle / Elysium / Now We Are Free 4. Chevaliers De Sangreal (The Da Vinci Code)",
                "The Lion King Medley: Circle Of Life (Prelude) / King Of Pride Rock (reprise)",
                "Pirates Of The Caribbean Medley: Captain Jack Sparrow / One Day / Up Is Down / He's A Pirate",
                "You're So Cool (True Romance)",
                "Rain Man: Main Theme",
                "What Are You Going To Do When You Are Not Saving The World (Man Of Steel)",
                "Journey To The Line (The Thin Red Line)",
                "The Electro Suite (themes from The Amazing Spider - Man 2)",
                "The Dark Knight Trilogy Medley: Why So Serious? / Like A Dog Chasing Cars / Why Do We Fall? / Introduce A Little Anarchy / The Fire Rises",
                "Aurora",
                "Interstellar Medley: Day One / Cornfield Chase / No Time For Caution / Stay",
                "Inception Medley: Half Remembered Dream / Dream Is Collapsing / Mombasa / Time"
        );
        albumRepository.save(new Album("Live In Prague",
                artistRepository.findFirstByName("Hans Zimmer"),
                tracks,
                new HashMap<String, Integer>(),
                0,
                "https://images-na.ssl-images-amazon.com/images/I/81goB-mjoZL._SL1500_.jpg",
                musicLabelRepository.findFirstByName("Ultra Records"),
                2016,
                2,
                "Hans Zimmer is one of the most successful film music composers working today. His multi-award winning career reaches back to the mid-80s and he has developed close working relationships with renowned directors such as Ridley Scott, Michael Bay, Ron Howard and Christopher Nolan. This concert was filmed on May 7th, 2016 in Prague during his hugely successful European concert tour. Hans was accompanied by a band, orchestra and choir, 72 musicians in total, including Johnny Marr of The Smiths.",
                new Date()));

        //Add album to genre
        genreSoundtracks = genreRepository.findFirstByName("Soundtracks");
        genreSoundtracksListOfAlbum = genreSoundtracks.getAlbumList();
        genreSoundtracksListOfAlbum.add(albumRepository.findFirstByName("Live In Prague"));
        genreSoundtracks.setAlbumList(genreSoundtracksListOfAlbum);
        genreRepository.save(genreSoundtracks);

        //
        //Michał Lorenc - Psy & Osaczony
        //

        tracks = Arrays.asList("Psy",
                "Kolysanka",
                "Mafia",
                "Ona",
                "Masakra",
                "Wiecz R",
                "Dom, Skrzypce, Harfa",
                "Zbrodnia",
                "Kolysanka II",
                "Korytarz, Krew",
                "Kraty",
                "Smierc Ubeka",
                "Kolysanie",
                "Umowa",
                "Toast",
                "Wojna",
                "Decyzja",
                "Zakochany Kundel",
                "Kolysanka IV",
                "Strach",
                "Smierc",
                "Oni",
                "Uwertura",
                "Kolysanka"
        );
        albumRepository.save(new Album("Psy & Osaczony",
                artistRepository.findFirstByName("Michał Lorenc"),
                tracks,
                new HashMap<String, Integer>(),
                0,
                "https://images-na.ssl-images-amazon.com/images/I/51%2BIkqQHp4L.jpg",
                musicLabelRepository.findFirstByName("E-music"),
                2005,
                2,
                "",
                new Date()));

        //Add album to genre
        genreSoundtracks = genreRepository.findFirstByName("Soundtracks");
        genreSoundtracksListOfAlbum = genreSoundtracks.getAlbumList();
        genreSoundtracksListOfAlbum.add(albumRepository.findFirstByName("Psy & Osaczony"));
        genreSoundtracks.setAlbumList(genreSoundtracksListOfAlbum);
        genreRepository.save(genreSoundtracks);

        //
        //Sylwia Grzeszczak - Komponujac Siebie
        //

        tracks = Arrays.asList("Księżniczka",
                                "Flirt",
                                "Schody",
                                "Pożyczony",
                                "Hotel chwil",
                                "Zaćmienie",
                                "Ucieknijmy stąd",
                                "Własny wzór",
                                "Nowy ty, nowa ja",
                                "Zdobywamy",
                                "Młody Bóg",
                                "Flagi serc",
                                "Kiedy tylko spojrzę"
        );
        albumRepository.save(new Album("Komponujac Siebie",
                artistRepository.findFirstByName("Sylwia Grzeszczak"),
                tracks,
                new HashMap<String, Integer>(),
                0,
                "https://images-na.ssl-images-amazon.com/images/I/61rVfWLoBxL.jpg",
                musicLabelRepository.findFirstByName("E-music"),
                2013,
                1,
                "Sylwia Grzeszczak singer, composer, songwriter ... in a word, a man orchestra ... she got to know a wider audience through cooperation with Liber and an amazing duo - a combination of youthful strength and experience, beautiful singing and playing the piano with electrifying rap, which next year in a row conquers the hearts of fans across Poland.\n" +
                        "\n" +
                        "Their biggest hits \"What will be with us\", \"New opportunities\" or \"We pass each other\" have long remained at the tops of the largest Polish radio stations such as Rmf Fm, Radio Zet or Radio Eska.\n" +
                        "\n" +
                        "For Sylwia, October 11 became an important date of 2011 - the day of the premiere of her first solo album Fri. \"Dream about future\". Even before the premiere, this album gained gold status, and less than two weeks later it was platinum, and to this day the album has sold almost 90,000 copies.",
                new Date()));

        //Add album to genre
        Genre genrePop = genreRepository.findFirstByName("Pop");
        List<Album> genrePopListOfAlbum = genrePop.getAlbumList();
        genrePopListOfAlbum.add(albumRepository.findFirstByName("Komponujac Siebie"));
        genreSoundtracks.setAlbumList(genrePopListOfAlbum);
        genreRepository.save(genrePop);


        //
        //Michael Jackson - Bad
        //

        tracks = Arrays.asList("Bad",
                                "The Way You Make Me Feel",
                                "Speed Demon",
                                "Liberian Girl",
                                "Just Good Friends",
                                "Another Part of Me",
                                "Man in the Mirror",
                                "I Just Can't Stop Loving You - Michael Jackson feat. Siedah Garrett",
                                "Dirty Diana",
                                "Smooth Criminal",
                                "Leave Me Alone"
        );
        albumRepository.save(new Album("Bad",
                artistRepository.findFirstByName("Michael Jackson"),
                tracks,
                new HashMap<String, Integer>(),
                0,
                "https://images-na.ssl-images-amazon.com/images/I/71xG%2BqsDb%2BL._SX522_.jpg",
                musicLabelRepository.findFirstByName("K Records"),
                1987,
                1,
                "Bad is Michael Jackson's 7th studio album, released by K Records on August 31, 1987.This album consists of 11 of Jackson's songs, one that features Stevie Wonder and another that features Siedah Garrett. Bad is the 19-27th most sold album worldwide with 65,000,000 sold.",
                new Date()));

        //Add album to genre
        genrePop = genreRepository.findFirstByName("Pop");
        genrePopListOfAlbum = genrePop.getAlbumList();
        genrePopListOfAlbum.add(albumRepository.findFirstByName("Bad"));
        genreSoundtracks.setAlbumList(genrePopListOfAlbum);
        genreRepository.save(genrePop);

        //
        //Michael Jackson - Thriller
        //

        tracks = Arrays.asList("Wanna Be Startin Somethin",
                                "Baby Be Mine",
                                "The Girl Is Mine",
                                "Thriller",
                                "Beat It (Single Version)",
                                "Billie Jean",
                                "Human Nature",
                                "P.Y.T. (Pretty Young Thing)",
                                "The Lady in My Life"
        );
        albumRepository.save(new Album("Thriller",
                artistRepository.findFirstByName("Michael Jackson"),
                tracks,
                new HashMap<String, Integer>(),
                0,
                "https://images-na.ssl-images-amazon.com/images/I/71Y24VlCZGL._SX522_.jpg",
                musicLabelRepository.findFirstByName("K Records"),
                1982,
                1,
                "Thriller is the sixth studio album by American singer Michael Jackson, released on November 30, 1982 by K Records. Reunited with Off the Wall producer Quincy Jones, Jackson was inspired to create an album where \"every song was a killer\". With the ongoing backlash against disco, Jackson moved in a new musical direction, incorporating pop, rock, R&B, post-disco, and funk. The album features a single guest appearance, with Paul McCartney becoming the first artist to be featured on Jackson's albums. Recording took place from April to November 1982 at Westlake Recording Studios in Los Angeles, with a production budget of $750,000.",
                new Date()));

        //Add album to genre
        genrePop = genreRepository.findFirstByName("Pop");
        genrePopListOfAlbum = genrePop.getAlbumList();
        genrePopListOfAlbum.add(albumRepository.findFirstByName("Thriller"));
        genreSoundtracks.setAlbumList(genrePopListOfAlbum);
        genreRepository.save(genrePop);

        //
        //Frank Sinatra - Best Of The Best
        //

        tracks = Arrays.asList("Come Fly With Me",
                                "You Make Me Feel So Young",
                                "My Funny Valentine",
                                "Witchcraft",
                                "Young At Heart",
                                "In the Wee Small Hours of the Morning",
                                "I've Got You Under My Skin",
                                "Three Coins In The Fountain",
                                "All the Way",
                                "The Lady Is a Tramp",
                                "One For My Baby (And One More For The Road)",
                                "Nice 'N' Easy",
                                "The Very Thought of You",
                                "Fly Me to the Moon (in Other Words)",
                                "Night and Day",
                                "My Kind of Town",
                                "It Was a Very Good Year",
                                "Strangers in the Night",
                                "Somethin' Stupid (Feat. Nancy Sinatra) with Nancy Sinatra",
                                "That's Life",
                                "It Had to Be You",
                                "Mack The Knife",
                                "My Way",
                                "Theme from New York, New York"
        );
        albumRepository.save(new Album("Best Of The Best",
                artistRepository.findFirstByName("Frank Sinatra"),
                tracks,
                new HashMap<String, Integer>(),
                0,
                "https://images-na.ssl-images-amazon.com/images/I/41t3vCjOH1L.jpg",
                musicLabelRepository.findFirstByName("Sony Music"),
                2011,
                1,
                "The album was promoted as having his classic hits from Sony Music together on one record for the first time ever. This 2 disc set is packaged in a lift top box and contains 5 collectible postcards of classic Sinatra images. The first disc contains the biggest hits from Frank Sinatra's career; the second disc contains the long out-of-print Live In '57 performance recorded in Seattle, Washington. Though this album was compiled and released by Sony Music, the song \"Night and Day\" presented here is not the Capitol version, but the 1962 Reprise version from the album Sinatra and Strings.",
                new Date()));

        //Add album to genre
        Genre genreJazz = genreRepository.findFirstByName("Jazz");
        List<Album> genreJazzListOfAlbum = genreJazz.getAlbumList();
        genreJazzListOfAlbum.add(albumRepository.findFirstByName("Best Of The Best"));
        genreJazz.setAlbumList(genreJazzListOfAlbum);
        genreRepository.save(genreJazz);
    }


    public void displayData(){
        System.out.println("Adding sample data");
        System.out.println("--------------------------------");
        genreRepository.findAll().forEach(System.out::println);
        System.out.println("--------------------------------");
        musicLabelRepository.findAll().forEach(System.out::println);
        System.out.println("--------------------------------");
        artistRepository.findAll().forEach(System.out::println);
        System.out.println("--------------------------------");
        albumRepository.findAll().forEach(System.out::println);
        System.out.println("--------------------------------");
        userService.getAllUsers().forEach(System.out::println);
        System.out.println("--------------------------------");
        userService.getAllRoles().forEach(System.out::println);
        System.out.println("--------------------------------");
    }


    public void deleteAll() {
        System.out.println("Deleting all records..");
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        genreRepository.deleteAll();
        musicLabelRepository.deleteAll();
        userService.deleteAllUsersAndRoles();
    }


    	/*
	public void findByFirstName(){
		System.out.println("Artist found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		artistRepository.findByFirstName("Alice").forEach(System.out::println);
		System.out.println();
	}

	public void findByLastName(){
		System.out.println("Artists found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		artistRepository.findByLastName("Smith").forEach(System.out::println);
		System.out.println();
	}

	public void findByRegex() {
		System.out.println("Finding by Regex - All with last name starting with ^Smi");
		System.out.println("--------------------------------");
		artistRepository.findCustomByRegExLastName("^Smi").forEach(System.out::println);
	}
	 */

}
