package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.UserPartEntity;

import java.util.List;

public interface UserPartsRepository extends BaseRepository<UserPartEntity> {

    /**
     * Возвращает деталь владельца.
     *
     * @param partColorId идентификатор цвета детали
     * @param userId      идентификатор владельца
     * @return деталь.
     */
    @Query(name = "findUserPartByUserAndPartColor")
    UserPartEntity findUserPartByUserAndPartColor(@Param("partColorId") Long partColorId,
                                                  @Param("userId") Long userId);

    /**
     * Возвразает все заведенные детали владельца (те, у которых отличается количество от суммы в наборах).
     *
     * @param userId идентификатор пользователя
     * @return детали владельца
     */
    @Query(name = "getAllUserPartsByUser", nativeQuery = true)
    List<UserPartEntity> getAllUserPartsByUser(@Param("userId") Long userId);

    /**
     * Возвращает количество указанной детали в наборах владельца.
     *
     * @param userId      идентификатор пользователя
     * @param partColorId идентификатор цвета детали
     */
    @Query(name = "getPartCountInUserSets")
    Long getPartCountInUserSets(@Param("userId") Long userId, @Param("partColorId") Long partColorId);
}
