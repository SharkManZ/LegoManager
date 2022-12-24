package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.legomanager.dao.dto.NumberDto;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.entity.PartNumberEntity;
import ru.shark.home.legomanager.dao.repository.PartNumberRepository;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class PartNumberDao extends BaseDao<PartNumberEntity> {

    private PartNumberRepository partNumberRepository;

    public PartNumberDao() {
        super(PartNumberEntity.class);
    }

    public List<PartNumberEntity> savePartNumbers(PartEntity partEntity, List<NumberDto> dtos) {
        if (partEntity == null) {
            throw new ValidationException("Не указана деталь");
        }
        if (isEmpty(dtos)) {
            throw new ValidationException("Не указаны номера детали");
        }

        List<PartNumberEntity> existsNumbers = partNumberRepository.getPartNumbersByPartId(partEntity.getId());
        if (!isEmpty(existsNumbers)) {
            Map<String, NumberDto> numbersMap = dtos.stream().collect(Collectors.toMap(item -> item.getNumber().toLowerCase(), item -> item));
            Map<String, PartNumberEntity> existsMap = existsNumbers.stream().collect(Collectors.toMap(item -> item.getNumber().toLowerCase(), item -> item));
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
                        savePartNumber(partEntity, item);
                    });

        } else {
            dtos.stream()
                    .forEach(item -> {
                        savePartNumber(partEntity, item);
                    });
        }
        return partNumberRepository.getPartNumbersByPartId(partEntity.getId());
    }

    private void savePartNumber(PartEntity partEntity, NumberDto dto) {
        PartNumberEntity entity = new PartNumberEntity();
        entity.setNumber(dto.getNumber());
        entity.setPart(partEntity);
        entity.setMain(dto.isMain());

        save(entity);
    }

    @Autowired
    public void setPartNumberRepository(PartNumberRepository partNumberRepository) {
        this.partNumberRepository = partNumberRepository;
    }
}
