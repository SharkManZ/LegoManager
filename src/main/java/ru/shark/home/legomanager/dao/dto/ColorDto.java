package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

public class ColorDto extends BaseDto {
    private String name;
    private String hexColor;

    public ColorDto(){
        // empty constructor
    }

    public ColorDto(String name, String hexColor) {
        this.name = name;
        this.hexColor = hexColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }
}
