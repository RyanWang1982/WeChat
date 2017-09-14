/**
 * 
 */
package wang.yongrui.wechat.property;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

/**
 * @author Wang Yongrui
 *
 */
@Configuration
@ConfigurationProperties
@PropertySource(value = { "classpath:TargetDomain.properties" }, ignoreResourceNotFound = true)
@Getter
public class TargetDomainAdditionalMap {

	public Map<String, String> additionalMap = new HashMap<>();

}
