package ru.shark.home.legomanager.dao.dto.export;

import java.util.List;

public class UserDictionaryDto {
    private String name;
    private List<UserSetDictionaryDto> sets;
    private List<UserPartDictionaryDto> parts;

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

    public List<UserPartDictionaryDto> getParts() {
        return parts;
    }

    public void setParts(List<UserPartDictionaryDto> parts) {
        this.parts = parts;
    }
}
