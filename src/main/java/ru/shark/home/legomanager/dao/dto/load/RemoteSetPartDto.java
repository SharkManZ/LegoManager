package ru.shark.home.legomanager.dao.dto.load;

public class RemoteSetPartDto {
    private Long id;
    private Integer count;
    private String number;
    private String colorNumber;
    private String name;
    private String imgUrl;
    private Long comparisonPartColorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(String colorNumber) {
        this.colorNumber = colorNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getComparisonPartColorId() {
        return comparisonPartColorId;
    }

    public void setComparisonPartColorId(Long comparisonPartColorId) {
        this.comparisonPartColorId = comparisonPartColorId;
    }
}
