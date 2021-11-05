package ru.shark.home.legomanager.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.dao.repository.SeriesRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Component
public class TestDataLoader {
    private static final ObjectMapper mapper = new JsonMapper();
    private static final List<String> cleanUpLst = Arrays.asList(
            "LEGO_SERIES"
    );

    @Autowired
    private ru.shark.home.legomanager.util.TestEntityFinder entityFinder;

    @Autowired
    private SeriesRepository seriesRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * Загружает файлы с данными для тестов.
     *
     * @param files массив
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadSeries(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<SeriesEntity> list = mapper.readValue(fl, new TypeReference<List<SeriesEntity>>() {
                });
                list.forEach(entity -> {
                    seriesRepository.save(entity);
                });
            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Удаляет все данные из определенного списка таблиц
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cleanUp() {
        cleanUpLst.forEach(item -> {
            em.createNativeQuery(" delete from " + item).executeUpdate();
        });
    }
}
