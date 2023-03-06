package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.load.PartLoadSkipEntity;

import java.util.List;

public interface PartLoadSkipRepository extends BaseRepository<PartLoadSkipEntity> {
    @Query(value = "select p from PartLoadSkipEntity p")
    List<PartLoadSkipEntity> getAllPartSkip();
}
