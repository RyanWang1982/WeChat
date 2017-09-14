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
public enum TargetDomain implements BasicEnum {

	All("All"), User("User"), Role("Role"), Privilege("Privilege");

	private String description;

	/**
	 * @param description
	 */
	private TargetDomain(String description) {
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
