package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.Genre;
import com.zbdihd.projectnosql.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, String> {

    Genre findFirstByName(String name);
    void deleteByName(String name);
    List<Genre> findByNameStartsWithIgnoreCase(String name);

}