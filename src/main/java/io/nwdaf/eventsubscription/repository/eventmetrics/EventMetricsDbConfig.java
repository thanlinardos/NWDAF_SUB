package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.util.HashMap;
import java.util.Objects;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
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
  entityManagerFactoryRef = "eventmetricsEntityManagerFactory",
  transactionManagerRef = "eventmetricsTransactionManager"
)
public class EventMetricsDbConfig {
	@Bean(name="eventmetricsDataSourceProperties")
	@ConfigurationProperties("eventmetrics.datasource")
	  public DataSourceProperties eventmetricsProperties() {
	      return new DataSourceProperties();
	  }
	@Bean(name = "eventmetricsDataSource")
	public DataSource eventmetricsDataSource(@Qualifier("eventmetricsDataSourceProperties") DataSourceProperties eventmetricsProperties) {
		return eventmetricsProperties
		    .initializeDataSourceBuilder()
		    .build();
		}
  
  @Bean(name = "eventmetricsEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean 
  eventmetricsEntityManagerFactory(@Qualifier("eventmetricsDataSource") DataSource dataSource,
		  @Qualifier("eventmetricsDataSourceProperties") DataSourceProperties dataSourceProperties
  ) {
	  final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	  LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
	  factoryBean.setDataSource(eventmetricsDataSource(dataSourceProperties));
	  factoryBean.setPackagesToScan("io.nwdaf.eventsubscription.repository.eventmetrics.entities");
      factoryBean.setJpaVendorAdapter(vendorAdapter);
      
      return factoryBean;
	  
  }
  @Bean(name = "eventmetricsTransactionManager")
//  public PlatformTransactionManager eventmetricsTransactionManager(
//    @Qualifier("eventmetricsEntityManagerFactory") LocalContainerEntityManagerFactoryBean 
//    eventmetricsEntityManagerFactory
//  ) {
//    return new JpaTransactionManager(Objects.requireNonNull(eventmetricsEntityManagerFactory.getObject()));
//  }
  public PlatformTransactionManager eventmetricsTransactionManager() {
      final JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(eventmetricsEntityManagerFactory(eventmetricsDataSource(eventmetricsProperties()),eventmetricsProperties()).getObject());
      return transactionManager;
  }
}