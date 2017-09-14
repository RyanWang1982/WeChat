/**
 * 
 */
package wang.yongrui.wechat.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author Wang Yongrui
 *
 */
public class PatchBeanUtils extends BeanUtils {

	/**
	 * @param source
	 * @param target
	 * @param isPutUpdate
	 */
	public static void updateProperties(Object source, Object target, boolean isPutUpdate) {
		if (isPutUpdate) {
			copyProperties(source, target);
		} else {
			patchProperties(source, target);
		}
	}

	/**
	 * Patch the not null property values of the given source bean into the
	 * given target bean.
	 * <p>
	 * Note: The source and target classes do not have to match or even be
	 * derived from each other, as long as the properties match. Any bean
	 * properties that the source bean exposes but the target bean does not will
	 * silently be ignored.
	 *
	 * @param source
	 *            the source bean
	 * @param target
	 *            the target bean
	 * @param ignoreProperties
	 *            array of property names to ignore
	 * @throws BeansException
	 *             if the patching failed
	 * @see BeanWrapper
	 */
	public static void patchProperties(Object source, Object target, String... ignoreProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		PropertyDescriptor[] targetPds = getPropertyDescriptors(target.getClass());
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if ((writeMethod != null) && ((ignoreList == null) || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if ((readMethod != null) && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0],
							readMethod.getReturnType())) {
						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(source);
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							// Only set not null value to the target object
							if (null != value) {
								writeMethod.invoke(target, value);
							}
						} catch (Throwable ex) {
							throw new FatalBeanException(
									"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
						}
					}
				}
			}
		}
	}

}
