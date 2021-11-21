package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.ColorEntity;

public interface ColorRepository extends BaseRepository<ColorEntity> {
    /**
     * Возвращает цвет по названию.
     *
     * @param name цвет
     * @return сущность цвета
     */
    @Query(name = "findColorByName")
    ColorEntity findColorByName(@Param("name") String name);
}
