/**
 * 
 */
package wang.yongrui.wechat.initializer;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wang.yongrui.wechat.entity.enumeration.TargetDomain;
import wang.yongrui.wechat.property.TargetDomainAdditionalMap;
import wang.yongrui.wechat.utils.EnumUtils;

/**
 * @author Wang Yongrui
 *
 */
@Component
public class TargetDomainInitializer {

	@Autowired
	private TargetDomainAdditionalMap targetDomainAdditionalMap;

	@PostConstruct
	public void initialize() {
		Map<String, String> additionalTargetDomainObject = targetDomainAdditionalMap.getAdditionalMap();
		for (Object key : additionalTargetDomainObject.keySet()) {
			EnumUtils.addEnum(TargetDomain.class, key.toString(), additionalTargetDomainObject.get(key));
		}
	}

}
