package com.softuni.angelovestates.model.DAO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ReviewDAO {

    private String author;
    private int rating;
    private String comment;
}
