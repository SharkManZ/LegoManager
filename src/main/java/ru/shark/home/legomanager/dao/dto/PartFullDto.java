package ru.shark.home.legomanager.dao.dto;

public class PartFullDto extends PartDto{
    private Integer colorsCount;
    private String minColorNumber;

    public Integer getColorsCount() {
        return colorsCount;
    }

    public void setColorsCount(Integer colorsCount) {
        this.colorsCount = colorsCount;
    }

    public String getMinColorNumber() {
        return minColorNumber;
    }

    public void setMinColorNumber(String minColorNumber) {
        this.minColorNumber = minColorNumber;
    }
}
