package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.dto.UserSetsSummaryDto;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.dao.repository.UsersRepository;

import java.text.MessageFormat;
import java.util.List;

import static ru.shark.home.common.common.ErrorConstants.EMPTY_ENTITY;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;

@Component
public class UsersDao extends BaseDao<UserEntity> {
    private static String NAME_FIELD = "name";

    private UsersRepository usersRepository;

    protected UsersDao() {
        super(UserEntity.class);
    }

    public PageableList<UserEntity> getWithPagination(RequestCriteria request) {
        Specification<UserEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(), NAME_FIELD);
        return usersRepository.getWithPagination(request, searchSpec, NAME_FIELD);
    }

    public List<UserEntity> getAllUsers() {
        return usersRepository.getAllUsers();
    }

    @Override
    public UserEntity save(UserEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(EMPTY_ENTITY, UserEntity.getDescription()));
        }

        UserEntity byName = usersRepository.findUserByName(entity.getName());
        if (byName != null && (entity.getId() == null || !entity.getId().equals(byName.getId()))) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                    UserEntity.getDescription(), entity.getName()));
        }

        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        UserEntity entity = findById(id);

        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                    UserEntity.getDescription(), id));
        }

        super.deleteById(id);
    }

    public UserSetsSummaryDto getUserSetsSummary(Long id) {
        return usersRepository.getUserSetsSummary(id);
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
