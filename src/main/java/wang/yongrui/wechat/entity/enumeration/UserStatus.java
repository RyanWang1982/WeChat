/**
 *
 */
package wang.yongrui.wechat.entity.enumeration;

import lombok.Getter;

/**
 * @author Wang Yongrui
 *
 */
@Getter
public enum UserStatus implements BasicEnum {

	New("New"), Active("Active"), Inactive("Inactive");

	private String description;

	/**
	 * @param description
	 */
	private UserStatus(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wang.yongrui.wechat.entity.enumeration.BasicEnum#getName()
	 */
	@Override
	public String getName() {
		return this.name();
	}

}
