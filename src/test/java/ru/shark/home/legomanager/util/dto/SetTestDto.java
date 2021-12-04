package ru.shark.home.legomanager.util.dto;

import java.util.List;

public class SetTestDto {
    private String name;
    private String number;
    private Integer year;
    private String series;
    private List<SetPartTestDto> parts;

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
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public List<SetPartTestDto> getParts() {
        return parts;
    }

    public void setParts(List<SetPartTestDto> parts) {
        this.parts = parts;
    }
}
