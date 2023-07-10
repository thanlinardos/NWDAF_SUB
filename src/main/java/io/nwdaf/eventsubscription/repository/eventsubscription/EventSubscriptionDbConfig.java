package io.nwdaf.eventsubscription.repository.eventsubscription;

import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.nwdaf.eventsubscription.repository.eventnotification.EventNotificationDbConfig;
import io.nwdaf.eventsubscription.repository.eventnotification.NnwdafNotificationTable;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "entityManagerFactory",
  transactionManagerRef = "transactionManager",
  basePackageClasses = NnwdafEventsSubscriptionTable.class
)
public class EventSubscriptionDbConfig {
	@Bean(name="dataSourceProperties")
	@Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties eventsubscriptionDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource eventsubscriptionDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
          .initializeDataSourceBuilder()
          .build();
    }	
	
  
  @Primary
  @Bean(name = "entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean 
  entityManagerFactory(
    EntityManagerFactoryBuilder builder,
    @Qualifier("dataSource") DataSource dataSource, @Qualifier("dataSourceProperties") DataSourceProperties dataSourceProperties
  ) {
	  HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	  LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
	  factoryBean.setDataSource(eventsubscriptionDataSource(dataSourceProperties));
      factoryBean.setJpaVendorAdapter(vendorAdapter);
      factoryBean.setJpaVendorAdapter(vendorAdapter);
      factoryBean.setPackagesToScan(EventSubscriptionDbConfig.class.getPackage().getName());
      return factoryBean;
  }
    
  @Primary
  @Bean(name = "transactionManager")
  public PlatformTransactionManager transactionManager(
    @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean 
    entityManagerFactory
  ) {
    return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
  }
}