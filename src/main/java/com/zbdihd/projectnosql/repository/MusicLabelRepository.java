package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.MusicLabel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MusicLabelRepository extends MongoRepository<MusicLabel, String> {

    MusicLabel findFirstByName(String name);
    void deleteByName(String name);
    List<MusicLabel> findByNameStartsWithIgnoreCase(String name);

}
