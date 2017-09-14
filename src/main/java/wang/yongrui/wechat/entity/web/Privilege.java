/**
 * 
 */
package wang.yongrui.wechat.entity.web;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import wang.yongrui.wechat.entity.basic.PrivilegeBasic;
import wang.yongrui.wechat.entity.jpa.PrivilegeEntity;

/**
 * @author Wang Yongrui
 *
 */
public class Privilege extends PrivilegeBasic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1523499133646747779L;

	/**
	 * 
	 */
	public Privilege() {
		super();
	}

	/**
	 * 
	 */
	public Privilege(PrivilegeEntity privilegeEntity) {
		super();
		if (null != privilegeEntity) {
			BeanUtils.copyProperties(privilegeEntity, this);
		}
	}

}
