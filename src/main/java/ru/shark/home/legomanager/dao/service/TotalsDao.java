package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.dto.TotalDto;
import ru.shark.home.legomanager.dao.repository.*;
import ru.shark.home.legomanager.services.dto.TotalsRequestDto;

import java.util.Optional;

@Component
public class TotalsDao {
    private SeriesRepository seriesRepository;
    private SetRepository setRepository;
    private PartRepository partRepository;
    private SetPartRepository setPartRepository;
    private UserSetsRepository userSetsRepository;
    private UserPartsRepository userPartsRepository;

    public TotalDto getSeriesTotal(TotalsRequestDto request) {
        TotalDto dto = new TotalDto();
        dto.setTotal(seriesRepository.getCount());
        if (request != null && request.getUserId() != null) {
            dto.setInStock(userSetsRepository.getUserSeriesCountByUserId(request.getUserId()));
        } else {
            dto.setInStock(0L);
        }

        return dto;
    }

    public TotalDto getSetsTotal(TotalsRequestDto request) {
        TotalDto dto = new TotalDto();
        dto.setTotal(setRepository.getCount());
        if (request != null && request.getUserId() != null) {
            dto.setInStock(userSetsRepository.getUserSetsCountByUserId(request.getUserId()));
        } else {
            dto.setInStock(0L);
        }
        return dto;
    }

    public TotalDto getPartsTotal(TotalsRequestDto request) {
        TotalDto dto = new TotalDto();
        dto.setTotal(partRepository.getPartsCount());
        if (request != null && request.getUserId() != null) {
            dto.setInStock(userPartsRepository.getUserPartsCountByUserId(request.getUserId()));
        } else {
            dto.setInStock(0L);
        }

        return dto;
    }

    public TotalDto getPartColorsTotal(TotalsRequestDto request) {
        TotalDto dto = new TotalDto();
        dto.setTotal(setPartRepository.getAllSetsPartsCount());
        if (request != null && request.getUserId() != null) {
            dto.setInStock(userPartsRepository.getUserPartColorsCountByUserId(request.getUserId()));
        } else {
            dto.setInStock(0L);
        }
        return dto;
    }

    @Autowired
    public void setSeriesRepository(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @Autowired
    public void setSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @Autowired
    public void setPartRepository(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Autowired
    public void setSetPartRepository(SetPartRepository setPartRepository) {
        this.setPartRepository = setPartRepository;
    }

    @Autowired
    public void setUserSetsRepository(UserSetsRepository userSetsRepository) {
        this.userSetsRepository = userSetsRepository;
    }

    @Autowired
    public void setUserPartsRepository(UserPartsRepository userPartsRepository) {
        this.userPartsRepository = userPartsRepository;
    }
}
