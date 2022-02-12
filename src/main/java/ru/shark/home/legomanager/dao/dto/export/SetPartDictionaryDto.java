package ru.shark.home.legomanager.dao.dto.export;

public class SetPartDictionaryDto {
    private String partColorNumber;
    private Integer count;

    public String getPartColorNumber() {
        return partColorNumber;
    }

    public void setPartColorNumber(String partColorNumber) {
        this.partColorNumber = partColorNumber;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
