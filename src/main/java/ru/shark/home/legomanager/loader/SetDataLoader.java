package ru.shark.home.legomanager.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.shark.home.legomanager.dao.dto.load.RemoteSetPartsDto;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.entity.SetPartEntity;
import ru.shark.home.legomanager.dao.repository.PartColorRepository;
import ru.shark.home.legomanager.dao.repository.SetPartRepository;
import ru.shark.home.legomanager.dao.repository.SetRepository;
import ru.shark.home.legomanager.dao.service.SetPartDao;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.shark.home.legomanager.common.ErrorConstants.*;

@Component
@Transactional(Transactional.TxType.REQUIRED)
public class SetDataLoader {

    private SetRepository setRepository;
    private SetPartDao setPartDao;
    private SetPartRepository setPartRepository;
    private PartColorRepository partColorRepository;

    public void loadSetParts(String setNumber, List<RemoteSetPartsDto> setParts) {
        SetEntity setEntity = validateSet(setNumber, setParts);
        for (RemoteSetPartsDto dto : setParts) {
            SetPartEntity entity = new SetPartEntity();
            entity.setSet(setEntity);
            Long partColorId = partColorRepository
                    .getPartColorIdByPartColorNumberAndPartNumber(dto.getNumber(), dto.getColorNumber().split(",")[0].trim());
            if (partColorId == null) {
                throw new ValidationException(MessageFormat.format(PART_NOT_FOUND, dto.getNumber(), dto.getColorNumber()));
            }
            entity.setPartColor(new PartColorEntity());
            entity.getPartColor().setId(partColorId);
            entity.setCount(dto.getCount());
            setPartDao.save(entity);
        }
    }

    private SetEntity validateSet(String setNumber, List<RemoteSetPartsDto> setParts) {
        if (isBlank(setNumber)) {
            throw new ValidationException(EMPTY_SET_NUMBER);
        }
        if (ObjectUtils.isEmpty(setParts)) {
            throw new ValidationException(EMPTY_IMPORT_PARTS);
        }
        SetEntity setEntity = setRepository.findSetByNumber(setNumber.trim());
        if (setEntity == null) {
            throw new ValidationException(MessageFormat.format(SET_NOT_FOUND, setNumber));
        }
        if (setPartRepository.getSetPartsCount(setEntity.getId()).compareTo(0L) != 0) {
            throw new ValidationException(SET_MUST_BE_EMPTY);
        }

        return setEntity;
    }

    @Autowired
    public void setSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @Autowired
    public void setSetPartDao(SetPartDao setPartDao) {
        this.setPartDao = setPartDao;
    }

    @Autowired
    public void setSetPartRepository(SetPartRepository setPartRepository) {
        this.setPartRepository = setPartRepository;
    }

    @Autowired
    public void setPartColorRepository(PartColorRepository partColorRepository) {
        this.partColorRepository = partColorRepository;
    }
}
