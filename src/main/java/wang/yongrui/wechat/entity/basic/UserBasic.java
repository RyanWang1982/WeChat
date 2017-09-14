/**
 *
 */
package wang.yongrui.wechat.entity.basic;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
import wang.yongrui.wechat.entity.enumeration.Gender;
import wang.yongrui.wechat.entity.enumeration.UserStatus;
import wang.yongrui.wechat.entity.enumeration.UserType;
import wang.yongrui.wechat.entity.fundamental.AuditingEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wang Yongrui
 *
 */
@MappedSuperclass
@Getter
@Setter
public class UserBasic extends AuditingEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String iasSPUserId;

	@Column(unique = true)
	private String iasUserProfileId;

	@Column(nullable = false, unique = true)
	private String loginName;

	@Column(unique = true)
	private String employeeNumber;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(unique = true)
	private String mobileNumber;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Gender gender;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Calendar dateOfBirth;

	@Column(unique = true)
	private String portraitId;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserStatus status;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserType type;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBasic other = (UserBasic) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
