package ru.shark.home.legomanager.util.dto;

import java.util.List;

public class UserTestDto {
    private String name;
    private List<UserSetTestDto> sets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserSetTestDto> getSets() {
        return sets;
    }

    public void setSets(List<UserSetTestDto> sets) {
        this.sets = sets;
    }
}
