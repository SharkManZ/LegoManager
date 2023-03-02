package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.legomanager.dao.dto.NumberDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorNumberEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.entity.PartNumberEntity;
import ru.shark.home.legomanager.dao.repository.PartColorNumberRepository;
import ru.shark.home.legomanager.dao.repository.PartNumberRepository;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class PartColorNumberDao extends BaseDao<PartColorNumberEntity> {

    private PartColorNumberRepository partColorNumberRepository;

    public PartColorNumberDao() {
        super(PartColorNumberEntity.class);
    }

    public List<PartColorNumberEntity> savePartColorNumbers(PartColorEntity partColorEntity, List<NumberDto> dtos) {
        if (partColorEntity == null) {
            throw new ValidationException("Не указан цвет детали");
        }
        if (isEmpty(dtos)) {
            throw new ValidationException("Не указаны номера цвета детали");
        }

        List<PartColorNumberEntity> existsNumbers = partColorNumberRepository
                .getPartColorNumbersByPartColorId(partColorEntity.getId());
        if (!isEmpty(existsNumbers)) {
            Map<String, NumberDto> numbersMap = dtos.stream()
                    .collect(Collectors.toMap(item -> item.getNumber().toLowerCase(), item -> item));
            Map<String, PartColorNumberEntity> existsMap = existsNumbers.stream()
                    .collect(Collectors.toMap(item -> item.getNumber().toLowerCase(), item -> item));
            // удаляем отсутствующие номера
            existsNumbers.stream().filter(item -> !numbersMap.containsKey(item.getNumber().toLowerCase()))
                    .forEach(this::delete);
            // обновляем существующие номера
            existsNumbers.stream().filter(item -> numbersMap.containsKey(item.getNumber().toLowerCase()))
                    .forEach(item -> {
                        NumberDto dto = numbersMap.get(item.getNumber().toLowerCase());
                        item.setMain(dto.isMain());
                        save(item);
                    });
            numbersMap.entrySet().stream().filter(item -> !existsMap.containsKey(item.getKey()))
                    .map(item -> item.getValue())
                    .forEach(item -> {
                        savePartNumber(partColorEntity, item);
                    });

        } else {
            dtos.stream()
                    .forEach(item -> {
                        savePartNumber(partColorEntity, item);
                    });
        }
        return partColorNumberRepository.getPartColorNumbersByPartColorId(partColorEntity.getId());
    }

    private void savePartNumber(PartColorEntity partColorEntity, NumberDto dto) {
        PartColorNumberEntity entity = new PartColorNumberEntity();
        entity.setNumber(dto.getNumber());
        entity.setPartColor(partColorEntity);
        entity.setMain(dto.isMain());

        save(entity);
    }

    @Autowired
    public void setPartColorNumberRepository(PartColorNumberRepository partColorNumberRepository) {
        this.partColorNumberRepository = partColorNumberRepository;
    }
}
