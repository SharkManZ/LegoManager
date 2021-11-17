package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.dto.TotalDto;
import ru.shark.home.legomanager.dao.repository.SeriesRepository;
import ru.shark.home.legomanager.dao.repository.SetRepository;

@Component
public class TotalsDao {
    private SeriesRepository seriesRepository;
    private SetRepository setRepository;

    public TotalDto getSeriesTotal() {
        TotalDto dto = new TotalDto();
        dto.setTotal(seriesRepository.getCount());
        dto.setInStock(0L);

        return dto;
    }

    public TotalDto getSetsTotal() {
        TotalDto dto = new TotalDto();
        dto.setTotal(setRepository.getCount());
        dto.setInStock(0L);
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
}
