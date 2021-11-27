package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.service.PartColorDao;

import java.util.List;

@Component
public class PartColorDataManager extends BaseDataManager<PartColorEntity, PartColorDto> {
    public PartColorDataManager(PartColorDao dao) {
        super(dao, PartColorDto.class);
    }

    public List<PartColorDto> getPartColorListByPartId(Long partId) {
        PartColorDao dao = (PartColorDao) getDao();
        return getConverterUtil().entityListToDtoList(dao.getPartColorListByPartId(partId), PartColorDto.class);
    }
}