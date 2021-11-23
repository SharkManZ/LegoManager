package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;

public interface PartCategoryRepository extends BaseRepository<PartCategoryEntity> {
    /**
     * Возвращает категорию детали по названию.
     *
     * @param name название категории
     * @return сущность категории
     */
    @Query(name = "findPartCategoryByName")
    PartCategoryEntity findPartCategoryByName(@Param("name") String name);
}
