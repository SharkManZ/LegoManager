package ru.shark.home.legomanager.dao.dto;

import ru.shark.home.common.dao.dto.BaseDto;

public class UserPartListDto extends BaseDto {
    private Long userId;
    private Long partColorId;
    private String number;
    private String alternateNumber;
    private String colorNumber;
    private String alternateColorNumber;
    private Integer userCount;
    private Integer setsCount;

    public UserPartListDto() {
        // empty constructor
    }

    public UserPartListDto(Long id, Long partColorId, Long userId, String number, String alternateNumber, String colorNumber,
                           String alternateColorNumber, Integer userCount, Integer setsCount) {
        super();
        setId(id);
        this.userId = userId;
        this.partColorId = partColorId;
        this.number = number;
        this.alternateNumber = alternateNumber;
        this.colorNumber = colorNumber;
        this.alternateColorNumber = alternateColorNumber;
        this.userCount = userCount;
        this.setsCount = setsCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPartColorId() {
        return partColorId;
    }

    public void setPartColorId(Long partColorId) {
        this.partColorId = partColorId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public String getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(String colorNumber) {
        this.colorNumber = colorNumber;
    }

    public String getAlternateColorNumber() {
        return alternateColorNumber;
    }

    public void setAlternateColorNumber(String alternateColorNumber) {
        this.alternateColorNumber = alternateColorNumber;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getSetsCount() {
        return setsCount;
    }

    public void setSetsCount(Integer setsCount) {
        this.setsCount = setsCount;
    }
}
