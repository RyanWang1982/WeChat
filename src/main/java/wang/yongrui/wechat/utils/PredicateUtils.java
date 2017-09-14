/**
 * 
 */
package wang.yongrui.wechat.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;

/**
 * @author Wang Yongrui
 *
 */
public class PredicateUtils {

	/**
	 * @param preciseEntity
	 * @param root
	 * @param criteriaBuilder
	 * @return
	 */
	public static Predicate preciseAndRestrictions(Object preciseEntity, Root<?> root,
			CriteriaBuilder criteriaBuilder) {
		Predicate restrictions = criteriaBuilder.conjunction();

		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(preciseEntity.getClass());
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String propertyName = propertyDescriptor.getName();
			Method readMethod = propertyDescriptor.getReadMethod();
			Object value;
			try {
				value = readMethod.invoke(preciseEntity);
				if (!(value instanceof Class) && null != value) {
					restrictions = criteriaBuilder.and(restrictions,
							criteriaBuilder.equal(root.get(propertyName), value));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		return restrictions;
	}

}
