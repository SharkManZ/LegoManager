package ru.shark.home.legomanager.datamanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.datamanager.BaseDataManager;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.legomanager.dao.dto.NumberDto;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorNumberEntity;
import ru.shark.home.legomanager.dao.repository.PartColorNumberRepository;
import ru.shark.home.legomanager.dao.service.PartColorDao;
import ru.shark.home.legomanager.services.dto.SearchDto;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PartColorDataManager extends BaseDataManager<PartColorEntity, PartColorDto> {

    private PartColorNumberRepository partColorNumberRepository;

    public PartColorDataManager(PartColorDao dao) {
        super(dao, PartColorDto.class);
    }

    public List<PartColorDto> getPartColorListByPartId(Long partId, Search search) {
        PartColorDao dao = (PartColorDao) getDao();
        List<PartColorDto> list = getConverterUtil().entityListToDtoList(dao.getPartColorListByPartId(partId, search), PartColorDto.class);
        Map<Long, List<NumberDto>> colorNumbers = getColorNumbers(list.stream().map(PartColorDto::getId).collect(Collectors.toSet()));
        for (PartColorDto dto : list) {
            List<NumberDto> numbers = colorNumbers.getOrDefault(dto.getId(), Collections.emptyList());
            dto.setNumber(numbers.stream().filter(NumberDto::isMain).findFirst().get().getNumber());
            dto.setAlternateNumber(numbers.stream().filter(item -> !item.isMain()).map(NumberDto::getNumber).collect(Collectors.joining(", ")));
        }

        return list;
    }

    private Map<Long, List<NumberDto>> getColorNumbers(Set<Long> ids) {
        Map<Long, List<PartColorNumberEntity>> entities = partColorNumberRepository.getPartColorNumbersByPartColorIds(ids)
                .stream()
                .collect(Collectors.groupingBy(item -> item.getPartColor().getId()));
        Map<Long, List<NumberDto>> result = new HashMap<>();
        for (Map.Entry<Long, List<PartColorNumberEntity>> entry : entities.entrySet()) {
            result.put(entry.getKey(), entry.getValue().stream()
                    .map(item -> getConverterUtil().entityToDto(item, NumberDto.class)).collect(Collectors.toList()));
        }

        return result;
    }

    public PartColorDto search(SearchDto dto) {
        PartColorDao dao = (PartColorDao) getDao();
        PartColorDto search = getConverterUtil().entityToDto(dao.search(dto), PartColorDto.class);
        List<PartColorNumberEntity> list = partColorNumberRepository.getPartColorNumbersByPartColorId(search.getId());
        search.setNumber(list
                .stream().filter(PartColorNumberEntity::getMain).findFirst().get().getNumber());
        search.setAlternateNumber(list
                .stream().filter(item -> !item.getMain()).map(PartColorNumberEntity::getNumber)
                .collect(Collectors.joining(", ")));

        return search;
    }

    public PartColorDto savePartColor(PartColorDto dto) {
        PartColorDao dao = (PartColorDao) getDao();
        return dao.savePartColor(dto);
    }

    @Autowired
    public void setPartColorNumberRepository(PartColorNumberRepository partColorNumberRepository) {
        this.partColorNumberRepository = partColorNumberRepository;
    }
}
