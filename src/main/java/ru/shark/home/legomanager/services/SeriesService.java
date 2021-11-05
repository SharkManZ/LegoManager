package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.SeriesDto;
import ru.shark.home.legomanager.datamanager.SeriesDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class SeriesService extends BaseLogicService {

    private SeriesDataManager seriesDataManager;

    public BaseResponse getList(PageRequest request) {
        BaseResponse response;
        try {
            response = new BaseResponse<>();
            response.setBody(seriesDataManager.getWithPagination(getCriteria(request, SeriesDto.class)));
            response.setSuccess(true);

        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка серий: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse save(SeriesDto dto) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(seriesDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении серии:" + e.getMessage());
        }

        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            seriesDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении серии: " + e.getMessage());
        }

        return response;
    }

    @Autowired
    public void setSeriesDataManager(SeriesDataManager seriesDataManager) {
        this.seriesDataManager = seriesDataManager;
    }
}
