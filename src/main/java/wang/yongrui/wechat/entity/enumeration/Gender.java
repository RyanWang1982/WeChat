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
public enum Gender implements BasicEnum {

	Unknown("Unknown"), Male("Male"), Female("Female");

	private String description;

	/**
	 * @param description
	 */
	private Gender(String description) {
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
