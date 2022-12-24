package ru.shark.home.legomanager.util.dto;

import java.util.List;

public class PartColorTestDto {
    private String color;
    private List<NumberTestDto> numbers;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<NumberTestDto> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<NumberTestDto> numbers) {
        this.numbers = numbers;
    }
}
