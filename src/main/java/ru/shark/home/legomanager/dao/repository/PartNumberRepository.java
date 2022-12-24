package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.PartNumberEntity;

import java.util.List;

public interface PartNumberRepository extends BaseRepository<PartNumberEntity> {

    /**
     * Возвращает номера детали ее идентификатору.
     *
     * @param partId идентификатор детали
     * @return список номеров
     */
    @Query(value = "select pn from PartNumberEntity pn where pn.part.id = :partId")
    List<PartNumberEntity> getPartNumbersByPartId(@Param("partId") Long partId);
}
