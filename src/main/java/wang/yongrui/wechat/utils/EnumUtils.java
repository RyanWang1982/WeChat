/**
 * 
 */
package wang.yongrui.wechat.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

/**
 * @author Wang Yongrui
 *
 */
@SuppressWarnings("restriction")
public class EnumUtils {

	private static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();

	@SuppressWarnings("unchecked")
	public static <E extends Enum<?>> void addEnum(Class<E> enumType, String enumName, String enumDescription) {
		// 0. Sanity checks
		if (!Enum.class.isAssignableFrom(enumType)) {
			throw new RuntimeException("class " + enumType + " is not an instance of Enum");
		}
		// 1. Lookup "$VALUES" holder in enum class and get previous enum
		// instances
		Field valuesField = null;
		Field[] fields = enumType.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().contains("$VALUES")) {
				valuesField = field;
				break;
			}
		}
		AccessibleObject.setAccessible(new Field[] { valuesField }, true);

		try {

			// 2. Copy it
			E[] previousValues = (E[]) valuesField.get(enumType);
			List<E> values = new ArrayList<>(Arrays.asList(previousValues));

			Class<?>[] additionalTypes = new Class<?>[] { String.class };
			Object[] additionalValues = new Object[] { enumDescription };
			// 3. build new enum
			E newValue = (E) makeEnum(enumType, enumName, values.size(), additionalTypes, additionalValues);

			// 4. add new value
			values.add(newValue);

			// 5. Set new values field
			setFailsafeFieldValue(valuesField, null, values.toArray((E[]) Array.newInstance(enumType, 0)));

			// 6. Clean enum cache
			cleanEnumCache(enumType);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes,
			Object[] additionalValues) throws Exception {
		Object[] parms = new Object[additionalValues.length + 2];
		parms[0] = value;
		parms[1] = Integer.valueOf(ordinal);
		System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
		return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
	}

	private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes)
			throws NoSuchMethodException {
		Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
		parameterTypes[0] = String.class;
		parameterTypes[1] = int.class;
		System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
		return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
	}

	private static void setFailsafeFieldValue(Field field, Object target, Object value)
			throws NoSuchFieldException, IllegalAccessException {

		// let's make the field accessible
		field.setAccessible(true);

		// next we change the modifier in the Field instance to
		// not be final anymore, thus tricking reflection into
		// letting us modify the static final field
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		int modifiers = modifiersField.getInt(field);

		// blank out the final bit in the modifiers int
		modifiers &= ~Modifier.FINAL;
		modifiersField.setInt(field, modifiers);

		FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
		fa.set(target, value);
	}

	private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
		// Sun (Oracle?!?) JDK 1.5/6
		blankField(enumClass, "enumConstantDirectory");
		// IBM JDK
		blankField(enumClass, "enumConstants");
	}

	private static void blankField(Class<?> enumClass, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		for (Field field : Class.class.getDeclaredFields()) {
			if (field.getName().contains(fieldName)) {
				AccessibleObject.setAccessible(new Field[] { field }, true);
				setFailsafeFieldValue(field, enumClass, null);
				break;
			}
		}
	}

}
