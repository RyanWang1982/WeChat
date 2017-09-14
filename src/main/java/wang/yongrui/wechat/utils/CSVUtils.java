/**
 * 
 */
package wang.yongrui.wechat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * @author Wang Yongrui
 *
 */
public class CSVUtils {

	/**
	 * @param csvInputStream
	 * @param entityClass
	 * @return
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public static <E> List<E> convertToEntityList(InputStream csvInputStream, Class<E> entityClass) throws IOException {
		CsvMapper csvMapper = new CsvMapper();
		CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();

		List<E> entityList = (List<E>) csvMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.readerFor(entityClass).with(csvSchema).readValues(csvInputStream).readAll();

		return entityList;
	}
}
