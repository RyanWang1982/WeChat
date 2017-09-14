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
public enum ActionPermission implements BasicEnum {

	All("All"), Create("Create"), Retrieve("Retrieve"), Update("Update"), Delete("Delete");

	private String description;

	/**
	 * @param description
	 */
	private ActionPermission(String description) {
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
