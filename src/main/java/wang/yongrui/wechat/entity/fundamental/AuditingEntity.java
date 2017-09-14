/**
 *
 */
package wang.yongrui.wechat.entity.fundamental;

import java.util.Calendar;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wang Yongrui
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AuditingEntity {

	@CreatedDate
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss SSS")
	private Calendar createdDate;

	@CreatedBy
	@JsonProperty(access = Access.READ_ONLY)
	private String createdBy;

	@LastModifiedDate
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss SSS")
	private Calendar lastModifiedDate;

	@LastModifiedBy
	@JsonProperty(access = Access.READ_ONLY)
	private String lastModifiedBy;

}
