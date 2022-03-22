package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

public class SetPartFullDto extends BaseDto {
    private PartColorDto partColor;
    private Long setId;
    private Long partColorId;
    private Integer count;
    private String colorNumber;
    private String alternateColorNumber;
    private String number;
    private String alternateNumber;
    private String partName;
    private String hexColor;

    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }

    public Long getPartColorId() {
        return partColorId;
    }

    public void setPartColorId(Long partColorId) {
        this.partColorId = partColorId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(String colorNumber) {
        this.colorNumber = colorNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getAlternateColorNumber() {
        return alternateColorNumber;
    }

    public void setAlternateColorNumber(String alternateColorNumber) {
        this.alternateColorNumber = alternateColorNumber;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public PartColorDto getPartColor() {
        return partColor;
    }

    public void setPartColor(PartColorDto partColor) {
        this.partColor = partColor;
    }
}