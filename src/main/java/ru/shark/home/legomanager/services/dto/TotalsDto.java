package ru.shark.home.legomanager.services.dto;

import ru.shark.home.legomanager.dao.dto.TotalDto;

public class TotalsDto {
    private TotalDto series;
    private TotalDto sets;
    private TotalDto diffParts;
    private TotalDto parts;

    public TotalDto getSeries() {
        return series;
    }

    public void setSeries(TotalDto series) {
        this.series = series;
    }

    public TotalDto getSets() {
        return sets;
    }

    public void setSets(TotalDto sets) {
        this.sets = sets;
    }

    public TotalDto getDiffParts() {
        return diffParts;
    }

    public void setDiffParts(TotalDto diffParts) {
        this.diffParts = diffParts;
    }

    public TotalDto getParts() {
        return parts;
    }

    public void setParts(TotalDto parts) {
        this.parts = parts;
    }
}
