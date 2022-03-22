package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

public class PartDto extends BaseDto {
    private String name;
    private String number;
    private String alternateNumber;
    private PartCategoryDto category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public PartCategoryDto getCategory() {
        return category;
    }

    public void setCategory(PartCategoryDto category) {
        this.category = category;
    }
}
