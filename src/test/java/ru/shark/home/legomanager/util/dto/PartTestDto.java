package ru.shark.home.legomanager.util.dto;

import java.util.List;

public class PartTestDto {
    private String name;
    private String number;
    private String alternateNumber;
    private String category;
    private List<PartColorTestDto> colors;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<PartColorTestDto> getColors() {
        return colors;
    }

    public void setColors(List<PartColorTestDto> colors) {
        this.colors = colors;
    }
}
