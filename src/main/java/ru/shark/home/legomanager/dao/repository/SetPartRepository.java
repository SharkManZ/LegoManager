package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.SetPartEntity;

import java.util.List;

public interface SetPartRepository extends BaseRepository<SetPartEntity> {

    /**
     * Возвращает все детали набора с сортировкой по категории детали и ее названию.
     *
     * @param setId идентификатор набора
     * @return список деталей
     */
    @Query(name = "getSetPartsBySetId")
    List<SetPartEntity> getSetPartsBySetId(@Param("setId") Long setId);

    /**
     * Возвращает деталь набора по идентификаторам набора и цвета детали.
     *
     * @param setId       идентификатор набора
     * @param partColorId идентификатор цвета детали
     * @return деталь набора
     */
    @Query(name = "getSetPartBySetAndPrtColorId")
    SetPartEntity getSetPartBySetAndPrtColorId(@Param("setId") Long setId, @Param("partColorId") Long partColorId);

    /**
     * Возвращает количество деталей всех наборов.
     *
     * @return количество
     */
    @Query(name = "getAllSetsPartsCount")
    Long getAllSetsPartsCount();

    /**
     * Возвращает количество видов деталей в наборе.
     *
     * @param setId идентификатор набора
     * @return количество видом деталей
     */
    @Query(name = "getSetPartsCount")
    Long getSetPartsCount(@Param("setId") Long setId);

    /**
     * Возвращает детали набора с сортировкой по номеру цвета детали.
     *
     * @param setId идентификатор набора
     * @return коллекция деталей наборов
     */
    @Query(value = "select sp from SetPartEntity sp where sp.set.id = :setId order by sp.partColor.number")
    List<SetPartEntity> findBySetId(@Param("setId") Long setId);
}
