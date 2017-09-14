/**
 * 
 */
package wang.yongrui.wechat.entity.web;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import wang.yongrui.wechat.entity.basic.RoleBasic;
import wang.yongrui.wechat.entity.jpa.PrivilegeEntity;
import wang.yongrui.wechat.entity.jpa.RoleEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wang Yongrui
 *
 */
public class Role extends RoleBasic implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7031045197248352420L;

	@Getter
	@Setter
	private Set<Privilege> privilegeSet;

	/**
	 * 
	 */
	public Role() {
		super();
	}

	/**
	 * 
	 */
	public Role(RoleEntity roleEntity) {
		super();
		if (null != roleEntity) {
			BeanUtils.copyProperties(roleEntity, this);
			if (CollectionUtils.isNotEmpty(roleEntity.getPrivilegeEntitySet())) {
				Set<Privilege> privilegeSet = new LinkedHashSet<Privilege>();
				for (PrivilegeEntity privilegeEntity : roleEntity.getPrivilegeEntitySet()) {
					privilegeSet.add(new Privilege(privilegeEntity));
				}
				setPrivilegeSet(privilegeSet);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@JsonIgnore
	@Override
	public String getAuthority() {
		return getName();
	}

}
