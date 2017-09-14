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
public enum UserType implements BasicEnum {

	Public("public"), Customer("customer"), Partner("partner"), Employee("employee");

	private String description;

	/**
	 * @param description
	 */
	private UserType(String description) {
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

	/**
	 * @param description
	 * @return
	 */
	public static UserType valueOfDescription(String description) {
		UserType result = null;
		for (UserType userType : UserType.values()) {
			if (userType.getDescription().equals(description)) {
				result = userType;
				break;
			}
		}

		return result;
	}

}
