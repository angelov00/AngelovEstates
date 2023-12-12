package com.softuni.angelovestates.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ReviewAddDTO {

    @NotNull
    int rating;

    @Size(min = 4, max = 30)
    String comment;

}
