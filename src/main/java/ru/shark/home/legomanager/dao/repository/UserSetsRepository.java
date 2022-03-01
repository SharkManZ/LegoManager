package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.UserSetEntity;

import java.util.List;

public interface UserSetsRepository extends BaseRepository<UserSetEntity> {
    /**
     * Возвращает набор владельца по идентификаторам владельца и набора.
     *
     * @param userId идентификатор владельца
     * @param setId  идентификатор набора
     * @return сущность набора владельца
     */
    @Query(name = "findUserSetByUserAndSet")
    UserSetEntity findUserSetByUserAndSet(@Param("userId") Long userId,
                                          @Param("setId") Long setId);

    /**
     * Возвращает список наборов по владельцу с сортировкой по названию набора.
     *
     * @param userId идентификатор владельца
     * @return список
     */
    @Query(name = "getAllByUser")
    List<UserSetEntity> getAllByUser(@Param("userId") Long userId);
}
