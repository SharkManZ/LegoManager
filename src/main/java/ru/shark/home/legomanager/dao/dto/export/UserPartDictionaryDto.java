package ru.shark.home.legomanager.dao.dto.export;

public class UserPartDictionaryDto {
    private String partNumber;
    private String partColorNUmber;
    private Integer count;

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartColorNUmber() {
        return partColorNUmber;
    }

    public void setPartColorNUmber(String partColorNUmber) {
        this.partColorNUmber = partColorNUmber;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
