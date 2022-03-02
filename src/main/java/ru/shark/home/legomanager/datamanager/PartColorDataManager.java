package ru.shark.home.legomanager.datamanager;

import org.springframework.stereotype.Component;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.service.PartColorDao;
import ru.shark.home.legomanager.services.dto.SearchDto;

import java.util.List;

@Component
public class PartColorDataManager extends BaseDataManager<PartColorEntity, PartColorDto> {
    public PartColorDataManager(PartColorDao dao) {
        super(dao, PartColorDto.class);
    }

    public List<PartColorDto> getPartColorListByPartId(Long partId, Search search) {
        PartColorDao dao = (PartColorDao) getDao();
        return getConverterUtil().entityListToDtoList(dao.getPartColorListByPartId(partId, search), PartColorDto.class);
    }

    public PartColorDto search(SearchDto dto) {
        PartColorDao dao = (PartColorDao) getDao();
        return getConverterUtil().entityToDto(dao.search(dto), PartColorDto.class);
    }
}
