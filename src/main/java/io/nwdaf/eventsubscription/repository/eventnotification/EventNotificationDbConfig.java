package io.nwdaf.eventsubscription.repository.eventnotification;

import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "eventnotificationEntityManagerFactory",
  transactionManagerRef = "eventnotificationTransactionManager",
  basePackageClasses = NnwdafNotificationTable.class
)
public class EventNotificationDbConfig {
	@Bean(name="eventnotificationDataSourceProperties")
	@ConfigurationProperties("eventnotification.datasource")
	  public DataSourceProperties eventNotificationProperties() {
	      return new DataSourceProperties();
	  }
	  @Bean(name = "eventnotificationDataSource")
	  public DataSource eventnotificationDataSource(DataSourceProperties eventNotificationProperties) {
		    return eventNotificationProperties
		      .initializeDataSourceBuilder()
		      .build();
		}
  
  @Bean(name = "eventnotificationEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean 
  eventnotificationEntityManagerFactory(@Qualifier("eventnotificationDataSource") DataSource dataSource,
		  @Qualifier("eventnotificationDataSourceProperties") DataSourceProperties dataSourceProperties
  ) {
	  HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	  LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
	  factoryBean.setDataSource(eventnotificationDataSource(dataSourceProperties));
      factoryBean.setJpaVendorAdapter(vendorAdapter);
      factoryBean.setJpaVendorAdapter(vendorAdapter);
      factoryBean.setPackagesToScan(EventNotificationDbConfig.class.getPackage().getName());
      return factoryBean;
	  
  }
  @Bean(name = "eventnotificationTransactionManager")
  public PlatformTransactionManager eventnotificationTransactionManager(
    @Qualifier("eventnotificationEntityManagerFactory") LocalContainerEntityManagerFactoryBean 
    eventnotificationEntityManagerFactory
  ) {
    return new JpaTransactionManager(Objects.requireNonNull(eventnotificationEntityManagerFactory.getObject()));
  }
}