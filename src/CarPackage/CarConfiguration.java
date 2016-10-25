package CarPackage;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * Created by Mati on 2016-10-25.
 */
@Configuration
public class CarConfiguration {
    @Bean
    DataSource dataSource() {
        BasicDataSource dataSourceConfig = new BasicDataSource();
        dataSourceConfig.setDriverClassName("org.postgresql.Driver");
        dataSourceConfig.setUrl("jdbc:postgresql://127.0.0.1:5432/postgres");
        dataSourceConfig.setUsername("postgres");
        //dataSourceConfig.setValidationQuery("SELECT 1");
        dataSourceConfig.setPassword("postgres");

        return dataSourceConfig;
    }

    @Bean(name = "entityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("CarPackage");
        return entityManagerFactoryBean;
    }

    @Bean public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        return adapter;
    }
}
