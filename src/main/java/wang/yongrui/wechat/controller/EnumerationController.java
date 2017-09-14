/**
 * 
 */
package wang.yongrui.wechat.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wang Yongrui
 *
 */
@RestController
@RequestMapping("/enumerations")
public class EnumerationController {

	@GetMapping
	public ResponseEntity<Map<String, List<Map<String, String>>>> getAllEnumerationMapInfo() {
		return new ResponseEntity<>(getAllEnumerationMap(), HttpStatus.OK);
	}

	/**
	 * @return
	 */
	private Map<String, List<Map<String, String>>> getAllEnumerationMap() {
		Map<String, List<Map<String, String>>> enumerations = new LinkedHashMap<>();

		Reflections reflections = new Reflections("wang.yongrui.wechat.entity.enumeration");
		@SuppressWarnings("rawtypes")
		Set<Class<? extends Enum>> enumClassSet = reflections.getSubTypesOf(Enum.class);
		for (Class<?> enumClass : enumClassSet) {
			List<Map<String, String>> enumList = new ArrayList<>();
			List<?> enumConstantList = CollectionUtils.arrayToList(enumClass.getEnumConstants());
			for (Object eachConstant : enumConstantList) {
				Map<String, String> enumConstantMap = new LinkedHashMap<>();
				Enum<?> enumObject = (Enum<?>) eachConstant;
				String enumDescription;
				try {
					enumDescription = (String) enumClass.getMethod("getDescription").invoke(enumObject);
					enumDescription = StringUtils.isNotBlank(enumDescription) ? enumDescription : enumObject.name();
					enumConstantMap.put("value", enumObject.name());
					enumConstantMap.put("description", enumDescription);
					enumList.add(enumConstantMap);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
			if (!CollectionUtils.isEmpty(enumList)) {
				enumerations.put(enumClass.getSimpleName(), enumList);
			}
		}
		return enumerations;
	}

}
