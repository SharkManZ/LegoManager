package ru.shark.home.legomanager.dao.dto.export;

public class PartLoadSkipDto {
    private String ptn;

    public PartLoadSkipDto() {
        // empty constructor
    }

    public PartLoadSkipDto(String ptn) {
        this.ptn = ptn;
    }

    public String getPtn() {
        return ptn;
    }

    public void setPtn(String ptn) {
        this.ptn = ptn;
    }
}
