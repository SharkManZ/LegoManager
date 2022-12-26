package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.service.TotalsDao;
import ru.shark.home.legomanager.services.dto.TotalsDto;
import ru.shark.home.legomanager.services.dto.TotalsRequestDto;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class TotalsService {

    private TotalsDao totalsDao;

    public BaseResponse getTotals(TotalsRequestDto dto) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(getTotalsData(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении сводных данных: " + e.getMessage());
        }

        return response;
    }

    private TotalsDto getTotalsData(TotalsRequestDto request) {
        TotalsDto dto = new TotalsDto();
        dto.setSeries(totalsDao.getSeriesTotal(request));
        dto.setSets(totalsDao.getSetsTotal(request));
        dto.setDiffParts(totalsDao.getPartsTotal(request));
        dto.setParts(totalsDao.getPartColorsTotal(request));
        return dto;
    }

    @Autowired
    public void setTotalsDao(TotalsDao totalsDao) {
        this.totalsDao = totalsDao;
    }
}
