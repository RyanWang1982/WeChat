/**
 * 
 */
package wang.yongrui.wechat.entity.web;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import wang.yongrui.wechat.entity.basic.ExtendedInfoBasic;
import wang.yongrui.wechat.entity.jpa.ExtendedInfoEntity;

/**
 * @author Wang Yongrui
 *
 */
public class ExtendedInfo extends ExtendedInfoBasic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8947331661794016700L;

	@JsonIgnore
	private String name;

	/**
	 * 
	 */
	public ExtendedInfo() {
		super();
	}

	/**
	 * 
	 */
	public ExtendedInfo(ExtendedInfoEntity extendedInfoEntity) {
		super();
		if (null != extendedInfoEntity) {
			BeanUtils.copyProperties(extendedInfoEntity, this);
		}
	}

}
