package com.softuni.angelovestates.model.DTO;

import com.softuni.angelovestates.model.enums.OfferTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class OfferAddDTO {

    @NotBlank
    @Size(min = 3, max = 20)
    private String title;

    private OfferTypeEnum offerType;

    @NotNull
    @Min(1)
    @Max(10000000)
    private int price;

    @NotNull
    @DecimalMin("1")
    @DecimalMax("100000")
    private double size;

    @Min(1)
    @NotNull
    private int rooms;

    @NotBlank
    @Size(min = 5)
    private String description;

    @NotBlank
    private String province;

    @NotBlank
    @Size(min = 5)
    private String address;

    private List<MultipartFile> photos;
}
