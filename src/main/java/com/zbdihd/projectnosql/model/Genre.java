package com.zbdihd.projectnosql.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Genre")
public class Genre {
    @Id
    public String id;

    @NonNull public String name;
    @NonNull public List<String> albumIDs;
}