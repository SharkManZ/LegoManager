package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.entity.SetEntity;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.dao.entity.UserSetEntity;
import ru.shark.home.legomanager.dao.repository.SetRepository;
import ru.shark.home.legomanager.dao.repository.UserSetsRepository;
import ru.shark.home.legomanager.dao.repository.UsersRepository;

import java.text.MessageFormat;

import static ru.shark.home.common.common.ErrorConstants.*;

@Component
public class UserSetsDao extends BaseDao<UserSetEntity> {
    private static final String SET_NAME_FIELD = "set.name";
    private static final String SET_NUMBER_FIELD = "set.number";

    private UserSetsRepository userSetsRepository;
    private UsersRepository usersRepository;
    private SetRepository setRepository;

    protected UserSetsDao() {
        super(UserSetEntity.class);
    }

    public PageableList<UserSetEntity> getWithPagination(Long userId, RequestCriteria request) {
        Specification<UserSetEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(),
                SET_NAME_FIELD, SET_NUMBER_FIELD);
        Specification<UserSetEntity> userSpec = SpecificationUtils.equalAttribute("user.id", userId);
        return userSetsRepository.getWithPagination(request, SpecificationUtils.andSpecifications(userSpec, searchSpec),
                SET_NAME_FIELD);
    }

    @Override
    public UserSetEntity save(UserSetEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(EMPTY_ENTITY, UserSetEntity.getDescription()));
        }

        usersRepository.findById(entity.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, UserEntity.getDescription(),
                        entity.getUser().getId())));
        SetEntity setEntity = setRepository.findById(entity.getSet().getId()).orElseThrow(
                () -> new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, SetEntity.getDescription(),
                        entity.getSet().getId())));

        UserSetEntity byIds = userSetsRepository.findUserSetByUserAndSet(entity.getUser().getId(),
                entity.getSet().getId());
        if (byIds != null && (entity.getId() == null || !entity.getId().equals(byIds.getId()))) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                    UserSetEntity.getDescription(), setEntity.getName()));
        }

        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        UserSetEntity entity = findById(id);

        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                    UserSetEntity.getDescription(), id));
        }

        super.deleteById(id);
    }

    @Autowired
    public void setUserSetsRepository(UserSetsRepository userSetsRepository) {
        this.userSetsRepository = userSetsRepository;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
    }
}
