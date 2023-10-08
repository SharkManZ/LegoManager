package ru.shark.home.legomanager.services.dto;

import ru.shark.home.legomanager.dao.dto.load.RemoteSetPartDto;

import java.util.List;

public class RemoteSetPartsDto {
    private int diffPartsCount;
    private int missingDiffPartsCount;
    private List<RemoteSetPartDto> parts;

    public int getDiffPartsCount() {
        return diffPartsCount;
    }

    public void setDiffPartsCount(int diffPartsCount) {
        this.diffPartsCount = diffPartsCount;
    }

    public int getMissingDiffPartsCount() {
        return missingDiffPartsCount;
    }

    public void setMissingDiffPartsCount(int missingDiffPartsCount) {
        this.missingDiffPartsCount = missingDiffPartsCount;
    }

    public List<RemoteSetPartDto> getParts() {
        return parts;
    }

    public void setParts(List<RemoteSetPartDto> parts) {
        this.parts = parts;
    }
}
