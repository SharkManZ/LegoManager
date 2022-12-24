package ru.shark.home.legomanager.util.dto;

import java.util.List;

public class PartTestDto {
    private String name;
    private List<NumberTestDto> numbers;
    private String category;
    private List<PartColorTestDto> colors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NumberTestDto> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<NumberTestDto> numbers) {
        this.numbers = numbers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<PartColorTestDto> getColors() {
        return colors;
    }

    public void setColors(List<PartColorTestDto> colors) {
        this.colors = colors;
    }
}
