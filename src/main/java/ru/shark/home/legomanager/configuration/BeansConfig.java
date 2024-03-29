package ru.shark.home.legomanager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.shark.home.common.dao.service.HqlQueryService;
import ru.shark.home.common.dao.service.SqlQueryService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class BeansConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public HqlQueryService hqlQueryService() {
        HqlQueryService queryService = new HqlQueryService();
        queryService.setEntityManager(entityManager);
        return queryService;
    }

    @Bean
    public SqlQueryService sqlQueryService() {
        return new SqlQueryService();
    }
}
