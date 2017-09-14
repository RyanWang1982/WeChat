/**
 * 
 */
package wang.yongrui.wechat.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;

/**
 * @author Wang Yongrui
 *
 */
@Configuration
public class SystemConfiguration {

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		return propertySourcesPlaceholderConfigurer;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
		return new HibernateJpaSessionFactoryBean();
	}

	@Bean
	public ConnectivityConfiguration connectivityConfiguration() {
		ConnectivityConfiguration connectivityConfiguration = null;
		try {
			Context context = new InitialContext();
			connectivityConfiguration = (ConnectivityConfiguration) context
					.lookup("java:comp/env/connectivityConfiguration");
		} catch (NamingException e) {
			// e.printStackTrace();
		}

		return connectivityConfiguration;
	}

}
