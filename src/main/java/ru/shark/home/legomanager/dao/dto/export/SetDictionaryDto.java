package ru.shark.home.legomanager.dao.dto.export;

import java.util.List;

public class SetDictionaryDto {
    private String number;
    private String name;
    private Integer year;
    private List<SetPartDictionaryDto> parts;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<SetPartDictionaryDto> getParts() {
        return parts;
    }

    public void setParts(List<SetPartDictionaryDto> parts) {
        this.parts = parts;
    }
}
