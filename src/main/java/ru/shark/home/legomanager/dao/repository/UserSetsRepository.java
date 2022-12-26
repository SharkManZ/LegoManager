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
    @Query(name = "getUserSetsByUser")
    List<UserSetEntity> getUserSetsByUser(@Param("userId") Long userId);

    /**
     * Возвращает количество серий по пользователю.
     *
     * @param userId идентификатор пользователя
     * @return количество серий
     */
    @Query(name = "getUserSeriesCountByUserId")
    Long getUserSeriesCountByUserId(@Param("userId") Long userId);

    /**
     * Возвращает количество наборов пользователя.
     *
     * @param userId идентификатор пользователя
     * @return количество наборов
     */
    @Query(name = "getUserSetsCountByUserId")
    Long getUserSetsCountByUserId(@Param("userId") Long userId);
}
