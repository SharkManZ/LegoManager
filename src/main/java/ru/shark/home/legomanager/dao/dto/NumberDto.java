package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.Dto;

public class NumberDto extends Dto {
    private String number;
    private boolean main;

    public NumberDto() {

    }

    public NumberDto(String number, boolean main) {
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
