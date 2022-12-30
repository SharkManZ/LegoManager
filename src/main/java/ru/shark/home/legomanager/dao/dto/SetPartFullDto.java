package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

//@EntityClass(clazz = SetPartEntity.class)
public class SetPartFullDto extends BaseDto {
    private Long setId;
    private Long partColorId;
    private Integer count;
    private String colorNumber;
    private String alternateColorNumber;
    private String number;
    private String alternateNumber;
    private String partName;
    private String hexColor;
    private String colorName;
    // TODO поля для поддержки фильтрации, переделать
    private Long colorId;
    private Long categoryId;

    public SetPartFullDto() {

    }

    public SetPartFullDto(Long id, Long setId, Long partColorId, Integer count,
                          String colorNumber, String alternateColorNumber, String number, String alternateNumber,
                          String partName, String hexColor, String colorName) {
        setId(id);
        this.setId = setId;
        this.partColorId = partColorId;
        this.count = count;
        this.colorNumber = colorNumber;
        this.alternateColorNumber = alternateColorNumber;
        this.number = number;
        this.alternateNumber = alternateNumber;
        this.partName = partName;
        this.hexColor = hexColor;
        this.colorName = colorName;
    }

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

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}