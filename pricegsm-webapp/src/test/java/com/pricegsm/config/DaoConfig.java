package com.pricegsm.config;

import com.pricegsm.dao.BaseEntityDao;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Configuration
@ComponentScan(basePackageClasses = BaseEntityDao.class, includeFilters = @ComponentScan.Filter({Repository.class}))
public class DaoConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocations(new Resource[]{
                new ClassPathResource("/persistence.properties"),
                new ClassPathResource("/build.properties")
        });
        return ppc;
    }
}
