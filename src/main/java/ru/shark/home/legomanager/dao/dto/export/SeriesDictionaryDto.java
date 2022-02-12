package ru.shark.home.legomanager.dao.dto.export;

import java.util.List;

public class SeriesDictionaryDto {
    private String name;
    private List<SetDictionaryDto> sets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SetDictionaryDto> getSets() {
        return sets;
    }

    public void setSets(List<SetDictionaryDto> sets) {
        this.sets = sets;
    }
}
