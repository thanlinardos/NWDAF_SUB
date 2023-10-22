// package io.nwdaf.eventsubscription.repository.redis;

// import javax.sql.DataSource;

// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// import org.springframework.orm.jpa.JpaTransactionManager;
// import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
// import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
// import org.springframework.transaction.PlatformTransactionManager;
// import org.springframework.transaction.annotation.EnableTransactionManagement;

// @Configuration
// @EnableTransactionManagement
// @EnableJpaRepositories(
//   entityManagerFactoryRef = "redisEntityManagerFactory",
//   transactionManagerRef = "redisTransactionManager"
// )
// public class RedisRepositoryConfig {
//     @Bean(name="redisDataSourceProperties")
// 	@ConfigurationProperties("redis.datasource")
// 	  public DataSourceProperties redisProperties() {
// 	      return new DataSourceProperties();
// 	  }
// 	@Bean(name = "redisDataSource")
// 	public DataSource redisDataSource(@Qualifier("redisDataSourceProperties") DataSourceProperties redisProperties) {
// 		return redisProperties
// 		    .initializeDataSourceBuilder()
// 		    .build();
// 		}
  
//   @Bean(name = "redisEntityManagerFactory")
//   public LocalContainerEntityManagerFactoryBean 
//   redisEntityManagerFactory(@Qualifier("redisDataSource") DataSource dataSource,
// 		  @Qualifier("redisDataSourceProperties") DataSourceProperties dataSourceProperties
//   ) {
// 	  final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
// 	  LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
// 	  factoryBean.setDataSource(redisDataSource(dataSourceProperties));
// 	  factoryBean.setPackagesToScan("io.nwdaf.eventsubscription.repository.redis.entities");
//       factoryBean.setJpaVendorAdapter(vendorAdapter);
      
//       return factoryBean;
	  
//   }
//   @Bean(name = "redisTransactionManager")
//   public PlatformTransactionManager redisTransactionManager() {
//       final JpaTransactionManager transactionManager = new JpaTransactionManager();
//       transactionManager.setEntityManagerFactory(redisEntityManagerFactory(redisDataSource(redisProperties()),redisProperties()).getObject());
//       return transactionManager;
//   }
// }
