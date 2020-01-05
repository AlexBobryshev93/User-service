package com.example.user_registration.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "companies")
@Data
public class Company {
    @Transient
    public static final String SEQUENCE_NAME = "companies_sequence";

    @Id
    private long id;

    @Indexed(unique = true)
    @Field(value = "company_name")
    private String name;
    private boolean foreign;
}
