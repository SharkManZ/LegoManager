package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

public class SetDto extends BaseDto {
    private String name;
    private String number;
    private Integer year;
    private SeriesDto series;

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

    public SeriesDto getSeries() {
        return series;
    }

    public void setSeries(SeriesDto series) {
        this.series = series;
    }
}
