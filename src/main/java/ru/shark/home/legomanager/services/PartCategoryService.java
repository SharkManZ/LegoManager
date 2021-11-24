package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.services.BaseLogicService;
import ru.shark.home.common.services.dto.PageRequest;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.dto.PartCategoryDto;
import ru.shark.home.legomanager.datamanager.PartCategoryDataManager;

import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class PartCategoryService extends BaseLogicService {

    private PartCategoryDataManager partCategoryDataManager;

    public BaseResponse getList(PageRequest request) {
        BaseResponse response;
        try {
            response = new BaseResponse<>();
            response.setBody(partCategoryDataManager.getWithPagination(getCriteria(request, PartCategoryDto.class)));
            response.setSuccess(true);

        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка категорий деталей: " + e.getMessage());
        }

        return response;
    }

    public BaseResponse getAllList() {
        BaseResponse response;
        try {
            response = new BaseResponse();
            response.setBody(partCategoryDataManager.getAllCategories());
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при получении списка всех категорий деталей: " +
                    e.getMessage());
        }

        return response;
    }

    public BaseResponse save(PartCategoryDto dto) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            response.setBody(partCategoryDataManager.save(dto));
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при сохранении категории детали:" + e.getMessage());
        }

        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response;

        try {
            response = new BaseResponse();
            partCategoryDataManager.deleteById(id);
            response.setSuccess(true);
        } catch (Exception e) {
            response = BaseResponse.buildError(ERR_500, "Ошибка при удалении категории детали: " + e.getMessage());
        }

        return response;
    }

    @Autowired
    public void setPartCategoryDataManager(PartCategoryDataManager partCategoryDataManager) {
        this.partCategoryDataManager = partCategoryDataManager;
    }
}
