package ru.shark.home.legomanager.dao.dto.load;

import ru.shark.home.common.dao.dto.BaseDto;

public class PartLoadSkipDto extends BaseDto {
    private Long id;
    private String pattern;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
