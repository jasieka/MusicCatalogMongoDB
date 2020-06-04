package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreRepository extends MongoRepository<Genre, String> {

    Genre findFirstByName(String name);

}