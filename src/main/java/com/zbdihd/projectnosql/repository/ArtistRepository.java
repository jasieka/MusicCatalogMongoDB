package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ArtistRepository extends MongoRepository<Artist, String> {

    List<Artist> findByName(String name);

    Artist findFirstByName(String name);
    void deleteByName(String name);
    List<Artist> findByNameStartsWithIgnoreCase(String name);
    /*
    @Query("{lastName:'?0'}")
    List<Artist> findCustomByLastName(String lastName);*/

    @Query("{name : { $regex: ?0 } }")
    List<Artist> findCustomByRegExName(String name);

}