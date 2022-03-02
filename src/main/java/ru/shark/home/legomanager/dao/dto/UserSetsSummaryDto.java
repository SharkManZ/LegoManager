package ru.shark.home.legomanager.dao.dto;

public class UserSetsSummaryDto {
    private Long partsCount;
    private Long uniquePartsCount;
    private Long colorsCount;

    public UserSetsSummaryDto() {
        // empty constructor
    }

    public UserSetsSummaryDto(Long partsCount, Long uniquePartsCount, Long colorsCount) {
        this.partsCount = partsCount;
        this.uniquePartsCount = uniquePartsCount;
        this.colorsCount = colorsCount;
    }

    public Long getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(Long partsCount) {
        this.partsCount = partsCount;
    }

    public Long getUniquePartsCount() {
        return uniquePartsCount;
    }

    public void setUniquePartsCount(Long uniquePartsCount) {
        this.uniquePartsCount = uniquePartsCount;
    }

    public Long getColorsCount() {
        return colorsCount;
    }

    public void setColorsCount(Long colorsCount) {
        this.colorsCount = colorsCount;
    }
}
