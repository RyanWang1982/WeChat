/**
 * 
 */
package wang.yongrui.wechat.utils;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;

import javax.persistence.Id;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

/**
 * @author Wang Yongrui
 *
 */
@Repository
public class JDBCBatchUpdateUtils {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private SessionFactory sessionFactory;

	public <E> boolean batchInsert(List<E> entityList) {
		boolean isSucceed = false;
		if (CollectionUtils.isNotEmpty(entityList)) {
			SqlParameterSource[] batchValues = SqlParameterSourceUtils.createBatch(entityList.toArray());
			String insertSQL = generateInsertSQL(entityList.get(0).getClass(), sessionFactory);
			int[] affectedRows = namedParameterJdbcTemplate.batchUpdate(insertSQL, batchValues);
			if (0 != affectedRows.length) {
				isSucceed = true;
			}
		}

		return isSucceed;
	}

	/**
	 * @param entityClass
	 * @return
	 */
	private static <E> String generateInsertSQL(Class<E> entityClass, SessionFactory sessionFactory) {
		AbstractEntityPersister abstractEntityPersister = ((AbstractEntityPersister) sessionFactory
				.getClassMetadata(entityClass));

		StringBuffer columnsStrBuffer = new StringBuffer(" (");
		StringBuffer valuesStrBuffer = new StringBuffer(" VALUES (");

		int index = 0;
		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(entityClass);
		for (PropertyDescriptor eachPropertyDescriptor : propertyDescriptors) {
			if (!eachPropertyDescriptor.getPropertyType().isSynthetic()
					&& !eachPropertyDescriptor.getPropertyType().isInstance(Class.class)
					&& !Collection.class.isAssignableFrom(eachPropertyDescriptor.getPropertyType())) {
				String eachPropertyName = eachPropertyDescriptor.getName();
				boolean isIdField = false;
				try {
					isIdField = entityClass.getSuperclass().getDeclaredField(eachPropertyName)
							.isAnnotationPresent(Id.class);
				} catch (Exception e) {
					continue;
				}
				if (!isIdField) {
					if (0 != index) {
						columnsStrBuffer.append(", ");
						valuesStrBuffer.append(", ");
					}

					columnsStrBuffer.append(abstractEntityPersister.getPropertyColumnNames(eachPropertyName)[0]);
					valuesStrBuffer.append(":").append(eachPropertyName);
					// All the enum properties must implements BasicEnum,
					// and the getName() method returning the this.name();
					if (eachPropertyDescriptor.getPropertyType().isEnum()) {
						valuesStrBuffer.append(".name");
					}

					index++;
				}

			}
		}
		columnsStrBuffer.append(") ");
		valuesStrBuffer.append(") ");

		StringBuffer insertStrBuffer = new StringBuffer("INSERT INTO ");
		insertStrBuffer.append(abstractEntityPersister.getTableName()).append(columnsStrBuffer).append(valuesStrBuffer);

		return insertStrBuffer.toString();
	}

}
