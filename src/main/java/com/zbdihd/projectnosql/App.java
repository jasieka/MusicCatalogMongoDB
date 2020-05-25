package com.zbdihd.projectnosql;

import com.zbdihd.projectnosql.model.Artist;
import com.zbdihd.projectnosql.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner{

	@Autowired
	private ArtistRepository artistRepository;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		deleteAll();
		addSampleData();
		listAll();
		findByFirstName();
		findByLastName();
		findByRegex();
	}

	public void deleteAll() {
		System.out.println("Deleting all records..");
		artistRepository.deleteAll();
	}

	public void addSampleData() {
		System.out.println("Adding sample data");
		artistRepository.save(new Artist("Alice", "Smith"));
		artistRepository.save(new Artist("Bob", "Smith"));
		artistRepository.save(new Artist("Bob", "Marley"));
	}

	public void listAll() {
		System.out.println("Listing sample data. Artists found with findAll():");
		System.out.println("-------------------------------");
		artistRepository.findAll().forEach(System.out::println);
		System.out.println();
	}

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

}
