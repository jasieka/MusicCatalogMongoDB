package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Album, String> {

    Album findFirstByName(String name);

}