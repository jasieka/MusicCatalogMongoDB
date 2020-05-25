package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.Artist;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

@Document(collection = "Artist")
public interface ArtistRepository extends MongoRepository<Artist, String> {

    public List<Artist> findByFirstName(String firstName);
    public List<Artist> findByLastName(String lastName);

    @Query("{lastName:'?0'}")
    List<Artist> findCustomByLastName(String lastName);

    @Query("{lastName : { $regex: ?0 } }")
    List<Artist> findCustomByRegExLastName(String lastName);

}