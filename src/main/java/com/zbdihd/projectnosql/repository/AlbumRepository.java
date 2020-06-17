package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.Album;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AlbumRepository extends MongoRepository<Album, String> {

    Album findFirstByName(String name);
    void deleteByName(String name);
    List<Album> findByNameStartsWithIgnoreCase(String name);

    @Query("{name : { $regex: ?0 } }")
    List<Album> findCustomByRegExName(String name);

}