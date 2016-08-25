package pl.vavatech.auction.blc;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

//https://docs.jboss.org/hibernate/orm/5.0/userGuide/en-US/html_single/#criteria
//https://docs.jboss.org/hibernate/orm/5.0/userGuide/en-US/html_single/#hql
@Configuration
@PropertySource(value = { "classpath:config.properties" })
public class HibernateStandaloneRunner {
	private static EntityManager em;
	@Value("${db.driverClassName}")
	private String driverClassName;
	@Value("${db.url}")
	private String url;
	@Value("${db.username}")
	private String username;
	@Value("${db.password}")
	private String password;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();
		emFactory.setPersistenceUnitName("auction");
		emFactory.setDataSource(dataSource());
		emFactory.setPackagesToScan(new String[] { "pl.vavatech.auction.blc.model" });
		emFactory.setJpaVendorAdapter(createHibernateAdapter());
		emFactory.getJpaPropertyMap().putAll(getHibernateProperties());
		return emFactory;
	}

	private HibernateJpaVendorAdapter createHibernateAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabase(Database.HSQL);
		return hibernateJpaVendorAdapter;
	}

	public Map<String, Object> getHibernateProperties() {
		Map<String, Object> properties = new HashMap();
		properties.put("hibernate.show_sql", true);
		properties.put("hibernate.format_sql", true);
		// properties.put("hibernate.ejb.interceptor",
		// HibernateEmptyInterceptor.class.getName());

		return properties;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;

	}

	static Long id;

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(HibernateStandaloneRunner.class);

		EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
		em = emf.createEntityManager();

		inTransaction("add", () -> {
		});
	}

	public static void inTransaction(String section, Runnable runnable) {
		System.out.println("############### " + section);
		em.getTransaction().begin();
		runnable.run();
		em.getTransaction().commit();
		em.clear();
		System.out.println("############### " + section);

	}

}
