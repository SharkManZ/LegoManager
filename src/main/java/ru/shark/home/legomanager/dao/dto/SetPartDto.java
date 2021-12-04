package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

public class SetPartDto extends BaseDto {
    private Integer count;
    private SetDto set;
    private PartColorDto partColor;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public SetDto getSet() {
        return set;
    }

    public void setSet(SetDto set) {
        this.set = set;
    }

    public PartColorDto getPartColor() {
        return partColor;
    }

    public void setPartColor(PartColorDto partColor) {
        this.partColor = partColor;
    }
}
