package ru.shark.home.legomanager.loader;

import org.hibernate.Session;
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
import ru.shark.home.legomanager.enums.LoadNumberType;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.shark.home.legomanager.common.ErrorConstants.EMPTY_IMPORT_PARTS;
import static ru.shark.home.legomanager.common.ErrorConstants.EMPTY_SET_NUMBER;
import static ru.shark.home.legomanager.common.ErrorConstants.PART_NOT_FOUND;
import static ru.shark.home.legomanager.common.ErrorConstants.SET_MUST_BE_EMPTY;
import static ru.shark.home.legomanager.common.ErrorConstants.SET_NOT_FOUND;

@Component
@Transactional(Transactional.TxType.REQUIRED)
public class SetDataLoader {
    private static final int BATCH_SIZE = 200;

    private SetRepository setRepository;
    private SetPartDao setPartDao;
    private SetPartRepository setPartRepository;
    private PartColorRepository partColorRepository;
    private EntityManager em;

    public List<RemoteSetPartsDto> findMissingParts(List<RemoteSetPartsDto> parts) {
        clearLoadTables();
        loadParts(parts);
        loadPartNumbers(parts);
        Set<Long> missingIds = (Set<Long>) em.createNamedQuery("getMissingLoadPartIds").getResultList().stream().map(item -> ((BigInteger) item).longValue())
                .collect(Collectors.toSet());
        clearLoadTables();
        return parts.stream().filter(item -> missingIds.contains(item.getId())).collect(Collectors.toList());
    }

    private void loadParts(List<RemoteSetPartsDto> parts) {
        em.unwrap(Session.class).doWork(connection -> {
            String insertString = "insert into LEGO_PART_LOAD_TABLE (LEGO_ID, LEGO_COUNT, LEGO_NAME, LEGO_URL) " +
                    " values (?, ?, ?, ?)";
            int rowNum = 1;
            try (PreparedStatement st = connection.prepareStatement(insertString)) {
                for (RemoteSetPartsDto part : parts) {
                    st.setLong(1, part.getId());
                    st.setInt(2, part.getCount());
                    st.setString(3, part.getName());
                    st.setString(4, part.getImgUrl());
                    st.addBatch();

                    if ((rowNum % BATCH_SIZE) == 0) {
                        st.executeBatch();
                        st.clearBatch();
                    }
                    rowNum++;
                }
                st.executeBatch();
            }
        });
    }

    private void clearLoadTables() {
        em.createNativeQuery("delete from LEGO_PART_LOAD_TABLE").executeUpdate();
        em.createNativeQuery("delete from LEGO_PART_LOAD_NUMBER_TABLE").executeUpdate();
    }

    private void loadPartNumbers(List<RemoteSetPartsDto> parts) {
        em.unwrap(Session.class).doWork(connection -> {
            String insertString = "insert into LEGO_PART_LOAD_NUMBER_TABLE (LEGO_PART_ID, LEGO_NUMBER, LEGO_TYPE) " +
                    " values (?, ?, ?)";
            int rowNum = 1;
            try (PreparedStatement st = connection.prepareStatement(insertString)) {
                for (RemoteSetPartsDto part : parts) {
                    rowNum = processStatement(st, part.getId(), part.getNumber().trim(), LoadNumberType.MAIN, rowNum);
                    for (String number : Arrays.asList(part.getColorNumber().split(","))) {
                        rowNum = processStatement(st, part.getId(), number.trim(), LoadNumberType.COLOR, rowNum);
                    }
                }
                st.executeBatch();
            }
        });
    }

    private int processStatement(PreparedStatement st, Long id, String number,
                                 LoadNumberType type, int rowNum) throws SQLException {
        st.setLong(1, id);
        st.setString(2, number);
        st.setString(3, type.name());
        st.addBatch();

        if ((rowNum % BATCH_SIZE) == 0) {
            st.executeBatch();
            st.clearBatch();
        }
        rowNum++;
        return rowNum;
    }

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

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }
}
