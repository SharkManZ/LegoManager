package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.SetDto;
import ru.shark.home.legomanager.datamanager.ColorDataManager;
import ru.shark.home.legomanager.datamanager.PartCategoryDataManager;
import ru.shark.home.legomanager.datamanager.SetDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class SetService extends BaseLogicService {
    private SetDataManager setDataManager;
    private ColorDataManager colorDataManager;
    private PartCategoryDataManager partCategoryDataManager;

    public BaseResponse getList(PageRequest request) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(setDataManager.getWithPagination(getCriteria(request, SetDto.class)));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка наборов: " +
                    e.getMessage());
        }

        return response;
    }

    public BaseResponse getList(PageRequest request, Long seriesId) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(setDataManager.getWithPagination(getCriteria(request, SetDto.class), seriesId));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка наборов по серии: " +
                    e.getMessage());
        }

        return response;
    }

    public BaseResponse save(SetDto dto) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(setDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении набора: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            setDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении набора: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse getSummary(Long id) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(setDataManager.getSummary(id));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении сводных данных набора: " +
                    e.getMessage());
        }

        return response;
    }

    public BaseResponse getSetColors(Long id) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(colorDataManager.getListBySetId(id));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении цветов набора: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse getSetPartCategories(Long id) {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(partCategoryDataManager.getListBySetId(id));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении видов деталей набора: " + e.getMessage());
        }

        return response;
    }

    @Autowired
    public void setSetDataManager(SetDataManager setDataManager) {
        this.setDataManager = setDataManager;
    }

    @Autowired
    public void setColorDataManager(ColorDataManager colorDataManager) {
        this.colorDataManager = colorDataManager;
    }

    @Autowired
    public void setPartCategoryDataManager(PartCategoryDataManager partCategoryDataManager) {
        this.partCategoryDataManager = partCategoryDataManager;
    }
}
