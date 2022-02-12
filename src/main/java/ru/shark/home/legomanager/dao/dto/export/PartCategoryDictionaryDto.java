package ru.shark.home.legomanager.dao.dto.export;

import java.util.List;

public class PartCategoryDictionaryDto {
    private String name;
    private List<PartDictionaryDto> parts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PartDictionaryDto> getParts() {
        return parts;
    }

    public void setParts(List<PartDictionaryDto> parts) {
        this.parts = parts;
    }
}
