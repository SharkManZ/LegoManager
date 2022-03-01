package ru.shark.home.legomanager.dao.dto.export;

import java.util.List;

public class UserDictionaryDto {
    private String name;
    private List<UserSetDictionaryDto> sets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserSetDictionaryDto> getSets() {
        return sets;
    }

    public void setSets(List<UserSetDictionaryDto> sets) {
        this.sets = sets;
    }
}
