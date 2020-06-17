package com.zbdihd.projectnosql.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document(collection = "MusicLabel")
public class MusicLabel {
    @Id
    private String id;

    private String name;
    private String dateOfCreation; //format dd/MM/yyyy
    private String countryOfResidence;
    private String chairmanOfTheBoard; //prezes zarządu

    private Date lastModifiedAt; //or created


    public MusicLabel(String name, String dateOfCreation, String countryOfResidence, String chairmanOfTheBoard, Date lastModifiedAt) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.countryOfResidence = countryOfResidence;
        this.chairmanOfTheBoard = chairmanOfTheBoard;
        this.lastModifiedAt = lastModifiedAt;
    }
}