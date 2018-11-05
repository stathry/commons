/**
 * 
 */
package org.stathry.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.annotation.convert.CDate;
import org.stathry.commons.annotation.convert.CMulti;
import org.stathry.commons.annotation.convert.CNum;
import org.stathry.commons.exception.DataConvertException;

/**
 * 
 * @author Demon
 * @date 2016年12月10日
 */
public final class DataConvertUtils{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataConvertUtils.class);
	
	/**
	 * list(object)转换为 list(map)
	 * @param beans
	 * @return
	 * @throws DataConvertException
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> convertBeansToMaps(List<?> beans) throws DataConvertException {
		if(beans == null) {
			return Collections.EMPTY_LIST;
		}
		List<Map<String, String>> listMaps = new ArrayList<>();
		for (Object bean : beans) {
			listMaps.add(convertBeanToMap(bean));
		}
		return listMaps;
	}
	
	/**
	 * bean转换为map
	 * @param bean
	 * @return
	 * @throws DataConvertException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> convertBeanToMap(Object bean) throws DataConvertException {
		if(bean == null) {
			return Collections.EMPTY_MAP;
		}
		Map<String, String> map = new HashMap<>();
		try {
			Field[] fs = bean.getClass().getDeclaredFields();
			if(fs != null && fs.length > 0) {
				for (Field f : fs) {
					String fname = f.getName();
					Object fvalue = PropertyUtils.getProperty(bean, fname);
					String value = String.valueOf(fvalue);
					
					CMulti cm = f.getAnnotation(CMulti.class);
					if(cm != null) {
						value = multiply(String.valueOf(fvalue), cm.multi(), cm.scale());
						map.put(fname, value);
						continue;
					}
					
					CDate cd = f.getAnnotation(CDate.class);
					if(cd != null) {
						if(fvalue instanceof Date) {
							value = DatetimeUtils.format((Date) fvalue, cd.pattern());
							map.put(fname, value);
							continue;
						}
					}
					
					if(fvalue == null) {
						value = "";
					}
					map.put(fname, value);
				}
			}
		} catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
			throw new DataConvertException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			LOGGER.warn(e.getMessage(), e);
		}
		return map;
	}
	
	public static <T1,T2> void convertBeans(List<T1> source, List<T2> target, Class<T2> targetClass) throws DataConvertException, InstantiationException, IllegalAccessException {
		if(source == null || target == null || source.isEmpty()) {
			LOGGER.warn("illegal argument.");
			throw new DataConvertException("illegal argument.");
		}
		
		for(int i = 0; i < source.size(); i++) {
			T2 o = (T2) targetClass.newInstance();
			convertBean(source.get(i), o);
			target.add(o);
		}
	}
	/**
	 * bean转换为bean(根据注解对基本数据类型、date、string作转换)
	 * @param source
	 * @param target
	 * @throws DataConvertException
	 */
	public static void convertBean(Object source, Object target) throws DataConvertException {
		if(source == null || target == null) {
			return;
		}
		
		try {
			Field[] sourceFields = source.getClass().getDeclaredFields();
			if(sourceFields != null && sourceFields.length > 0) {
				for (Field sourceField : sourceFields) {
					setProperty(sourceField, source, target);
				}
			}
		} 
		catch (NoSuchMethodException | NoSuchFieldException e) {
			LOGGER.debug("no such field[{}].",e.getMessage());
		}
		catch (Exception e) {
			DataConvertException ex = new DataConvertException("data convert error.", e);
			LOGGER.error(ex.getMessage(),ex);
			throw ex;
		}
	}

	public static String multiply(String amount, double multi, int scale) {
		if(StringUtils.isBlank(amount)) {
			return "0";
		}
		BigDecimal srcAmt = new BigDecimal(amount);
		srcAmt = srcAmt.multiply(new BigDecimal(String.valueOf(multi)));
		srcAmt = srcAmt.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return srcAmt.toString();
	}
	
	/**
	 * 
	 * @param sourceField
	 * @param source
	 * @param target
	 * @return isContinue
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static void setProperty(Field sourceField, Object source, Object target) throws Exception  {
		if(sourceField == null || source == null || target == null) {
			LOGGER.warn("invalid params field[{}], srcBean[{}], destBean[{}]", new Object[]{sourceField, source, target});
			return;
		}
		try {
			String sourceFieldName = sourceField.getName();
			Object sourceFieldValue = PropertyUtils.getProperty(source, sourceFieldName);
			Field targetField = target.getClass().getDeclaredField(sourceFieldName);
			if(targetField == null) {
				LOGGER.warn("fieldName[{}] of class[{}] not match.", sourceFieldName, sourceField.getClass());
				return;
			}
			
			if(sourceField.getType() == targetField.getType() || sourceFieldValue == null) {
				PropertyUtils.setProperty(target, sourceFieldName, sourceFieldValue);
				return;
			}
			
			Object targetFieldValue = null;
			CNum cnum = sourceField.getAnnotation(CNum.class);
			if(cnum != null) {
				if(sourceFieldValue instanceof String) {
					targetFieldValue = strToNum(targetField, (String) sourceFieldValue);
				} else {
					targetFieldValue = String.valueOf(sourceFieldValue);
				}
				PropertyUtils.setProperty(target, sourceFieldName, targetFieldValue);
				return ;
			}
			
			CMulti cmulti = sourceField.getAnnotation(CMulti.class);
			if(cmulti != null) {
				targetFieldValue = multiply(String.valueOf(sourceFieldValue), cmulti.multi(), cmulti.scale());
				if(sourceFieldValue instanceof String) {
					targetFieldValue = strToNum(targetField, (String) targetFieldValue);
				}
				PropertyUtils.setProperty(target, sourceFieldName, targetFieldValue);
				return ;
			}
			
			CDate cdate = sourceField.getAnnotation(CDate.class);
			if(cdate != null) {
				if(sourceFieldValue instanceof Date) {
					targetFieldValue = DatetimeUtils.format((Date) sourceFieldValue, cdate.pattern());
					PropertyUtils.setProperty(target, sourceFieldName, targetFieldValue);
				} else if(sourceFieldValue instanceof String){
					PropertyUtils.setProperty(target, sourceFieldName, DatetimeUtils.parse((String) sourceFieldValue, cdate.pattern()));
				}
				return ;
			}
			
			if(sourceFieldValue == null && targetField.getType() == String.class) {
				targetFieldValue = "";
			}
			PropertyUtils.setProperty(target, sourceFieldName, targetFieldValue);
		}
		 catch (NoSuchMethodException | NoSuchFieldException e) {
			 LOGGER.debug("no such field[{}].",e.getMessage());
			}
		catch (SecurityException | IllegalAccessException e) {
			LOGGER.warn("illegal access field[{}].",e.getMessage());
		}
		catch (Exception e) {
			LOGGER.error("set property error,class:{}, field:{}",source.getClass().getName(), sourceField.getName());
			LOGGER.error("set property error, cause {}", e.getMessage());
			throw e;
		}
	}
	
	public static Object strToNum(Field targetField, String sourceFieldValue) {
		Object value;
		if(targetField.getType() == Integer.class) {
			value = Integer.parseInt(sourceFieldValue);
		} 
		else if(targetField.getType() == Long.class) {
			value = Long.parseLong(sourceFieldValue);
		}
		else if(targetField.getType() == Double.class) {
			value = Double.parseDouble(sourceFieldValue);
		}
		else {
			LOGGER.warn("number convertor not supported type[{}].", targetField.getType());
			return null;
		}
		return value;
	}
	
}
