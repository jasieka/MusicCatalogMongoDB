package com.zbdihd.projectnosql;

import com.zbdihd.projectnosql.model.Album;
import com.zbdihd.projectnosql.model.Artist;
import com.zbdihd.projectnosql.model.Genre;
import com.zbdihd.projectnosql.model.MusicLabel;
import com.zbdihd.projectnosql.repository.AlbumRepository;
import com.zbdihd.projectnosql.repository.ArtistRepository;
import com.zbdihd.projectnosql.repository.GenreRepository;
import com.zbdihd.projectnosql.repository.MusicLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class App implements CommandLineRunner{

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private MusicLabelRepository musicLabelRepository;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		deleteAll();
		addSampleData();
	}

	public String getDate(int day, int month, int year)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
	}

	public void deleteAll() {
		System.out.println("Deleting all records..");
		albumRepository.deleteAll();
		artistRepository.deleteAll();
		genreRepository.deleteAll();
		musicLabelRepository.deleteAll();
	}

	public void addSampleData() {
		//Genre
		genreRepository.save(new Genre("Jazz", new ArrayList<String>()));
		genreRepository.save(new Genre("Classical", new ArrayList<String>()));
		genreRepository.save(new Genre("Pop", new ArrayList<String>()));
		genreRepository.save(new Genre("Rock", new ArrayList<String>()));
		genreRepository.save(new Genre("Soundtracks", new ArrayList<String>()));
		genreRepository.save(new Genre("Rap & Hip-Hop", new ArrayList<String>()));

		//Music Label
		musicLabelRepository.save(new MusicLabel("E-music", getDate(2, Calendar.FEBRUARY, 2014),
				"Poland", "Jacek Kowalczyk", new Date()));
		musicLabelRepository.save(new MusicLabel("Ultra Records", getDate(28, Calendar.MAY, 2007),
				"USA", "Patrick Moxey", new Date()));
		musicLabelRepository.save(new MusicLabel("Warner Music Group", getDate(15, Calendar.JULY, 1958),
				"USA", "Dannii Minogue", new Date()));
		musicLabelRepository.save(new MusicLabel("K Records", getDate(25, Calendar.FEBRUARY, 1975),
				"Canada", "Calvin Johnson", new Date()));
		musicLabelRepository.save(new MusicLabel("Chronological Classics", getDate(10, Calendar.DECEMBER, 1989),
				"France", "Gilles Pétard", new Date()));
		musicLabelRepository.save(new MusicLabel("Sony Music", getDate(6, Calendar.MAY, 1990),
				"USA", "Ton Bryan", new Date()));

		//Artist/Band
		artistRepository.save(new Artist("Michael Jackson", "USA",
				getDate(29, Calendar.AUGUST, 1958), getDate(25, Calendar.JUNE, 2009), new Date()));
		artistRepository.save(new Artist("Hans Zimmer", "Germany",
				getDate(12, Calendar.SEPTEMBER, 1957), "", new Date()));
		artistRepository.save(new Artist("Amy Winehouse", "USA",
				getDate(14, Calendar.SEPTEMBER, 1983), getDate(23, Calendar.JULY, 2011), new Date()));
		artistRepository.save(new Artist("Michał Lorenc", "Poland",
				getDate(5, Calendar.OCTOBER, 1955), "", new Date()));
		artistRepository.save(new Artist("Frank Sinatra", "USA",
				getDate(12, Calendar.DECEMBER, 1915), getDate(14, Calendar.MAY, 1998), new Date()));
		artistRepository.save(new Artist("Bryan Adams", "Canada",
				getDate(12, Calendar.NOVEMBER, 1959), "", new Date()));
		artistRepository.save(new Artist("Sylwia Grzeszczak", "Poland",
				getDate(7, Calendar.APRIL, 1989), "", new Date()));

		//Album

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
										artistRepository.findFirstByName("Hans Zimmer").getId(),
										tracks,
										new HashMap<String, String>(),
										0,
										"https://images-na.ssl-images-amazon.com/images/I/41VzuDJLCXL.jpg",
										musicLabelRepository.findFirstByName("Sony Music").getId(),
										2014,
										1,
										"Interstellar pairs the creative forces of Hans Zimmer and esteemed director Christopher Nolan, who collaborated previously on The Dark Knight film trilogy and Inception. Chris wanted us to push the limits, offers Zimmer. Every conversation was about pushing boundaries and exploring new territories. This movie virtually dictates that you put everything on the line and keep the laboratory doors wide open and experiment to the very end. It tested our limits: the limits of what musicians are capable of, the limits of what could be recorded, the limits of everyone's stamina, commitment and invention, and I think we got it.\n" +
												"\n" +
												"I believe that Hans score for Interstellar has the tightest bond between music and image that we've yet achieved, Nolan reflected. And we're excited for people to be able to revisit the soundtrack once they ve had the chance to experience the music in the film itself.",
										new Date()
										));
		//Add album to genre
		Genre genreSoundtracks = genreRepository.findFirstByName("Soundtracks");
		List<String> genreSoundtracksListOfAlbumIDs = genreSoundtracks.albumIDs;
		genreSoundtracksListOfAlbumIDs.add(albumRepository.findFirstByName("Interstellar").getId());
		genreSoundtracks.setAlbumIDs(genreSoundtracksListOfAlbumIDs);
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
				artistRepository.findFirstByName("Hans Zimmer").getId(),
				tracks,
				new HashMap<String, String>(),
				0,
				"https://images-na.ssl-images-amazon.com/images/I/81goB-mjoZL._SL1500_.jpg",
				musicLabelRepository.findFirstByName("Ultra Records").getId(),
				2016,
				2,
				"Hans Zimmer is one of the most successful film music composers working today. His multi-award winning career reaches back to the mid-80s and he has developed close working relationships with renowned directors such as Ridley Scott, Michael Bay, Ron Howard and Christopher Nolan. This concert was filmed on May 7th, 2016 in Prague during his hugely successful European concert tour. Hans was accompanied by a band, orchestra and choir, 72 musicians in total, including Johnny Marr of The Smiths.",
				new Date()));

		//Add album to genre
		genreSoundtracks = genreRepository.findFirstByName("Soundtracks");
		genreSoundtracksListOfAlbumIDs = genreSoundtracks.albumIDs;
		genreSoundtracksListOfAlbumIDs.add(albumRepository.findFirstByName("Live In Prague").getId());
		genreSoundtracks.setAlbumIDs(genreSoundtracksListOfAlbumIDs);
		genreRepository.save(genreSoundtracks);




		//Display
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
