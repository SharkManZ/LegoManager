package ru.shark.home.legomanager.dao.dto;

public class SeriesFullDto extends SeriesDto {
    private Long setsCount;
    private String imgName;

    public Long getSetsCount() {
        return setsCount;
    }

    public void setSetsCount(Long setsCount) {
        this.setsCount = setsCount;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
