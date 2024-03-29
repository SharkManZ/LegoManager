package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.dao.dto.request.UserPartListRequest;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.dao.entity.UserPartEntity;
import ru.shark.home.legomanager.dao.repository.PartColorRepository;
import ru.shark.home.legomanager.dao.repository.UserPartsRepository;
import ru.shark.home.legomanager.dao.repository.UsersRepository;
import ru.shark.home.legomanager.enums.UserPartRequestType;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.shark.home.common.common.ErrorConstants.EMPTY_ENTITY;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;

@Component
public class UserPartsDao extends BaseDao<UserPartEntity> {

    private UserPartsRepository userPartsRepository;
    private UsersRepository usersRepository;
    private PartColorRepository partColorRepository;

    protected UserPartsDao() {
        super(UserPartEntity.class);
    }

    public PageableList<UserPartListDto> getList(UserPartListRequest requestDto, RequestCriteria request) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", requestDto.getUserId());
        params.put("added", UserPartRequestType.ONLY_ADDED.equals(requestDto.getRequestType()) ? 1 : 0);
        params.put("notAdded", UserPartRequestType.ONLY_NOT_ADDED.equals(requestDto.getRequestType()) ? 1 : 0);
        params.put("notEquals", UserPartRequestType.NOT_EQUALS.equals(requestDto.getRequestType()) ? 1 : 0);
        params.put("lowerCount", UserPartRequestType.LOWER_COUNT.equals(requestDto.getRequestType()) ? 1 : 0);
        List<String> searchFields = Arrays.asList("colorNumber", "alternateColorNumber", "number", "alternateNumber",
                "categoryName", "partName");
        return getNativeWithPagination("getUserPartsByUser", request, params, searchFields,
                "getUserPartsByUserMapping");
    }

    @Override
    public UserPartEntity save(UserPartEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(EMPTY_ENTITY, UserPartEntity.getDescription()));
        }
        boolean isNew = entity.getId() == null;
        usersRepository.findById(entity.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, UserEntity.getDescription(),
                        entity.getUser().getId())));
        PartColorEntity partColor = partColorRepository.findById(entity.getPartColor().getId()).orElseThrow(
                () -> new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartColorEntity.getDescription(),
                        entity.getPartColor().getId())));
        UserPartEntity existsEntity = userPartsRepository.findUserPartByUserAndPartColor(partColor.getId(), entity.getUser().getId());
        if (existsEntity != null && (entity.getId() == null || !entity.getId().equals(existsEntity.getId()))) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                    UserPartEntity.getDescription(),
                    partColor.getNumbers().stream().filter(item -> item.getMain())
                            .findFirst().orElseThrow(() -> new ValidationException("Не найден главный номер цвета детали"))
                            .getNumber()));
        }

        Long countInSets = userPartsRepository.getPartCountInUserSets(entity.getUser().getId(), entity.getPartColor().getId());
        if (countInSets != null) {
            entity.setCount(entity.getCount() - countInSets.intValue());
        }
        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        UserPartEntity byId = findById(id);

        if (byId == null) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                    UserPartEntity.getDescription(), id));
        }

        super.deleteById(id);
    }

    @Autowired
    public void setUserPartsRepository(UserPartsRepository userPartsRepository) {
        this.userPartsRepository = userPartsRepository;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setPartColorRepository(PartColorRepository partColorRepository) {
        this.partColorRepository = partColorRepository;
    }
}
