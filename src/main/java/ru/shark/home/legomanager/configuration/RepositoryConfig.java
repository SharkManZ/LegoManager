package ru.shark.home.legomanager.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.shark.home.common.dao.repository.BaseRepositoryFactoryBean;
import ru.shark.home.common.dao.repository.JpaBaseRepository;

@Configuration
@EnableJpaRepositories(basePackages = "ru.shark.home.legomanager.dao.repository",
        repositoryBaseClass = JpaBaseRepository.class, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
public class RepositoryConfig {
}
