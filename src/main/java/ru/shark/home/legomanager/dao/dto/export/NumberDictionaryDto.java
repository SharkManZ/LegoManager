package ru.shark.home.legomanager.dao.dto.export;

public class NumberDictionaryDto {
    private String number;
    private boolean main;

    public NumberDictionaryDto(String number, boolean main) {
        this.number = number;
        this.main = main;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}
