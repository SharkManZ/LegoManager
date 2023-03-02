package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.PartEntity;

import java.util.List;

public interface PartRepository extends BaseRepository<PartEntity> {
    /**
     * Возвращает деталь по номеру.
     *
     * @param number номер.
     * @return сущность детали.
     */
    @Query(name = "findPartByNumber")
    PartEntity findPartByNumber(@Param("number") String number);

    /**
     * Возвращает количество деталей по идентификатору категории.
     *
     * @param categoryId идентификатор категории
     * @return количество наборов
     */
    @Query(name = "getPartCountByCategory")
    Long getPartCountByCategory(@Param("categoryId") Long categoryId);

    /**
     * Возвращает общее количество деталей.
     */
    @Query(name = "getPartsCount")
    Long getPartsCount();

    /**
     * Возвращает все детали по идентификатору категории.
     */
    @Query(value = "select p from PartEntity p join p.numbers pn with pn.main = true " +
            "where p.category.id = :categoryId order by pn.number")
    List<PartEntity> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Возвращает идентификаторы деталей для переданного списка номеров.
     */
    @Query(name = "getPartIdsByNumbers")
    List<Long> getPartIdsByNumbers(@Param("numbers") List<String> numbers);

    /**
     * Возвращает количество цветов детали.
     *
     * @param partId идентификатор детали
     * @return кол-во
     */
    @Query(name = "getPartColorsCountByPartId")
    Long getPartColorsCountByPartId(@Param("partId") Long partId);
}
