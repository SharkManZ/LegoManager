package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.ColorEntity;

import java.util.List;

public interface ColorRepository extends BaseRepository<ColorEntity> {
    /**
     * Возвращает цвет по названию.
     *
     * @param name цвет
     * @return сущность цвета
     */
    @Query(name = "findColorByName")
    ColorEntity findColorByName(@Param("name") String name);

    /**
     * Возвращает список всех цветов с сортировкой по названию.
     *
     * @return список
     */
    @Query(name = "getAllColors")
    List<ColorEntity> getAllColors();

    /**
     * Возвращает набор уникальных цветов из деталей набора.
     *
     * @param setId идентификатор набора
     * @return список цветов
     */
    @Query(name = "getColorsBySetId")
    List<ColorEntity> getColorsBySetId(@Param("setId") Long setId);

    /**
     * Возвращает цвета, которых нет в указанном виде детали.
     *
     * @param partId идентификатор вида детали
     * @return список цветов
     */
    @Query(name = "getNotAddedPartColorsByPartId")
    List<ColorEntity> getNotAddedPartColorsByPartId(@Param("partId") Long partId);
}
