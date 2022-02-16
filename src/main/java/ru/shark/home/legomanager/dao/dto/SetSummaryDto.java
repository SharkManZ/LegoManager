package ru.shark.home.legomanager.dao.dto;

import java.util.List;

public class SetSummaryDto {
    private String name;
    private String number;
    private Integer Year;
    private Integer partsCount;
    private Integer uniquePartsCount;
    private List<ColorDto> colors;

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

    public Integer getYear() {
        return Year;
    }

    public void setYear(Integer year) {
        Year = year;
    }

    public Integer getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(Integer partsCount) {
        this.partsCount = partsCount;
    }

    public Integer getUniquePartsCount() {
        return uniquePartsCount;
    }

    public void setUniquePartsCount(Integer uniquePartsCount) {
        this.uniquePartsCount = uniquePartsCount;
    }

    public List<ColorDto> getColors() {
        return colors;
    }

    public void setColors(List<ColorDto> colors) {
        this.colors = colors;
    }
}
