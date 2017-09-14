/**
 * 
 */
package wang.yongrui.wechat.entity.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import wang.yongrui.wechat.entity.basic.UserBasic;
import wang.yongrui.wechat.entity.jpa.ExtendedInfoEntity;
import wang.yongrui.wechat.entity.jpa.RoleEntity;
import wang.yongrui.wechat.entity.jpa.UserEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wang Yongrui
 *
 */
public class User extends UserBasic implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6176438686196620165L;

	@Getter
	@Setter
	private Set<Role> roleSet;

	@Getter
	@Setter
	private Map<String, ExtendedInfo> extendedInfoMap;

	/**
	 * 
	 */
	public User() {
		super();
	}

	/**
	 * 
	 */
	public User(UserEntity userEntity) {
		super();
		if (null != userEntity) {
			BeanUtils.copyProperties(userEntity, this);
			if (CollectionUtils.isNotEmpty(userEntity.getRoleEntitySet())) {
				Set<Role> roleSet = new LinkedHashSet<>();
				for (RoleEntity roleEntity : userEntity.getRoleEntitySet()) {
					roleSet.add(new Role(roleEntity));
				}
				setRoleSet(roleSet);
			}
			if (CollectionUtils.isNotEmpty(userEntity.getExtendedInfoEntityList())) {
				Map<String, ExtendedInfo> extendedInfoMap = new HashMap<>();
				for (ExtendedInfoEntity extendedInfoEntity : userEntity.getExtendedInfoEntityList()) {
					extendedInfoMap.put(extendedInfoEntity.getName(), new ExtendedInfo(extendedInfoEntity));
				}
				setExtendedInfoMap(extendedInfoMap);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@JsonIgnore
	@Override
	public String getUsername() {
		return this.getLoginName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@JsonIgnore
	@Override
	public String getPassword() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getAuthorities(
	 * )
	 */
	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getRoleSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#
	 * isAccountNonExpired()
	 */
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#
	 * isAccountNonLocked()
	 */
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#
	 * isCredentialsNonExpired()
	 */
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

}
