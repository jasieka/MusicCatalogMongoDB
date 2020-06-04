package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.MusicLabel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MusicLabelRepository extends MongoRepository<MusicLabel, String> {

    MusicLabel findFirstByName(String name);

}
