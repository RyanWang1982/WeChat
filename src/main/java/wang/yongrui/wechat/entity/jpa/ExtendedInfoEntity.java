/**
 * 
 */
package wang.yongrui.wechat.entity.jpa;

import javax.persistence.Entity;

import wang.yongrui.wechat.entity.basic.ExtendedInfoBasic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wang Yongrui
 *
 */
@Entity(name = "EXTENDED_INFO")
@Getter
@Setter
public class ExtendedInfoEntity extends ExtendedInfoBasic {

}
