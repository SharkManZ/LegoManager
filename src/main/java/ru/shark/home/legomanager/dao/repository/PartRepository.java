package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.PartEntity;

import java.util.List;
import java.util.Map;

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
     * Возвращает количество деталей по идентификатору категории
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
     * Возвращает дополнительные данные по деталям:
     * количество цветов
     * один из кодов цветов деталей (минимальный по номеру)
     */
    @Query(name = "getPartAdditionalDataByIds")
    List<Map<String, Object>> getPartAdditionalDataByIds(@Param("ids") List<Long> ids);
}
