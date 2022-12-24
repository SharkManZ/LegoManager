package ru.shark.home.legomanager.dao.dto.export;

import java.util.List;

public class PartDictionaryDto {
    private String name;
    private List<String> colors;
    private List<NumberDictionaryDto> numbers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NumberDictionaryDto> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<NumberDictionaryDto> numbers) {
        this.numbers = numbers;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }
}
