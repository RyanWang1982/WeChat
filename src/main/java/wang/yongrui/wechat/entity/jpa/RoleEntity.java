/**
 *
 */
package wang.yongrui.wechat.entity.jpa;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import wang.yongrui.wechat.entity.basic.RoleBasic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wang Yongrui
 *
 */
@Entity(name = "ROLE")
@Getter
@Setter
public class RoleEntity extends RoleBasic {

	@ManyToMany
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private Set<UserEntity> userEntitySet;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "ROLE_PRIVILEGE", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "privilege_id") })
	private Set<PrivilegeEntity> privilegeEntitySet;

}
