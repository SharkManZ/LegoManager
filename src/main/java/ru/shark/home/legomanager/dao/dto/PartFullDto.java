package ru.shark.home.legomanager.dao.dto;

import java.util.List;

public class PartFullDto extends PartDto{
    private Integer colorsCount;
    private String minColorNumber;
    private List<ColorDto> colors;

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

    public List<ColorDto> getColors() {
        return colors;
    }

    public void setColors(List<ColorDto> colors) {
        this.colors = colors;
    }
}
