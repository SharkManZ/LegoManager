package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.TotalDto;
import ru.shark.home.legomanager.dao.service.TotalsDao;
import ru.shark.home.legomanager.services.dto.TotalsDto;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class TotalsService {

    private TotalsDao totalsDao;

    public BaseResponse getTotals() {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(getTotalsData());
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении сводных данных: " + e.getMessage());
        }

        return response;
    }

    private TotalsDto getTotalsData() {
        TotalsDto dto = new TotalsDto();
        dto.setSeries(totalsDao.getSeriesTotal());
        dto.setSets(new TotalDto(0L, 0L));
        dto.setDiffParts(new TotalDto(0L, 0L));
        dto.setParts(new TotalDto(0L, 0L));
        return dto;
    }

    @Autowired
    public void setTotalsDao(TotalsDao totalsDao) {
        this.totalsDao = totalsDao;
    }
}
