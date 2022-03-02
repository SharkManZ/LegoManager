package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.dto.UserSetsSummaryDto;
import ru.shark.home.legomanager.dao.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UsersRepository extends BaseRepository<UserEntity> {
    /**
     * Возвращает владельца по названию.
     *
     * @param name цвет
     * @return сущность владельца
     */
    @Query(name = "findUserByName")
    UserEntity findUserByName(@Param("name") String name);

    /**
     * Возвращает список всех владельцев с сортировкой по названию
     *
     * @return список
     */
    @Query(name = "getAllUsers")
    List<UserEntity> getAllUsers();

    /**
     * Возвращает сводную информацию по наборам пользователя.
     *
     * @param userId идентификатор пользователя
     * @return сводная информация
     */
    @Query(name = "getUserSetsSummary")
    UserSetsSummaryDto getUserSetsSummary(@Param("userId") Long userId);
}
