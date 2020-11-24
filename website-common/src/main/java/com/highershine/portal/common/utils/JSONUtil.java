package com.highershine.portal.common.utils;
/**
 * JSONUtil.java
 * Date: 2013-7-19 下午3:30:55
 * Version: 1.0
 * Copyright(c) 2013 by Highershine
 */

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONUtil {

    static Logger log = LoggerFactory.getLogger(JSONUtil.class);

    /**
     * 将Json字符串解析为Map
     *
     * @param jsonString
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJsonToMap(String jsonString) throws ParseException {
        Map<String, Object> result = null;
        JSONParser parser = new JSONParser();
        if (!StringUtils.isEmpty(jsonString)) {
            result = (Map<String, Object>) parser.parse(jsonString);
        }
        return result;
    }

    /**
     * 将json字符串解析为List
     *
     * @param jsonString
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public static List<Object> parseJsonToList(String jsonString) throws ParseException {
        List<Object> result = null;
        if (!StringUtils.isEmpty(jsonString)) {
            try {
                result = (List<Object>) new JSONParser().parse(jsonString);
            } catch (ParseException e) {
                log.error("parseJsonToList", e);
                throw e;
            }
        }
        return result;
    }

    /**
     * 将Json字符串转换为Map
     *
     * @param jsonString
     * @return
     * @throws ParseException
     */
    public static Map<String, String> jSONParser(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = (JSONObject) jsonArray.get(i);
            map.put(obj.get("name").toString(), obj.get("value").toString());
        }
        return map;
    }

    /**
     * 将对象转换为Json字符串
     * <br>主方法
     *
     * @param obj
     * @return
     */
    public static String objectTojson(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        } else if (obj instanceof String || obj instanceof Integer
                || obj instanceof Float || obj instanceof Boolean
                || obj instanceof Short || obj instanceof Double
                || obj instanceof Long || obj instanceof BigDecimal
                || obj instanceof BigInteger || obj instanceof Byte) {
            json.append("\"").append(stringTojson(obj.toString())).append("\"");
        } else if (obj instanceof Object[]) {
            json.append(arrayTojson((Object[]) obj));
        } else if (obj instanceof List) {
            json.append(listTojson((List<?>) obj));
        } else if (obj instanceof Map) {
            json.append(mapTojson((Map<?, ?>) obj));
        } else if (obj instanceof Set) {
            json.append(setTojson((Set<?>) obj));
        } else {
            json.append(beanTojson(obj));
        }
        return json.toString();
    }


    /**
     * Map转换为Json字符串
     * <p> 注意：Value值会被转成String类型。
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String mapTojson(Map<?, ?> map) {
		/*StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(objectTojson(key));
				json.append(":");
				json.append(objectTojson(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}*/
        String json = null;
        json = JSONObject.toJSONString((Map) map);
        return json.toString();
    }

    /**
     * List转换为Json字符串
     *
     * @param list
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String mapListTojson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(JSONObject.toJSONString((Map) obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * 数组转换为Json字符串
     *
     * @param array
     * @return
     */
    public static String arrayTojson(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(objectTojson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * List转换为Json字符串
     *
     * @param list
     * @return
     */
    public static String listTojson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(objectTojson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * SET集合转换为json字符串
     *
     * @param set
     * @return
     */
    public static String setTojson(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(objectTojson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * Bean实体转换为Json字符串
     *
     * @param bean
     * @return
     */
    public static String beanTojson(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class)
                    .getPropertyDescriptors();
        } catch (IntrospectionException e) {
        }
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = objectTojson(props[i].getName());
                    String value = objectTojson(props[i].getReadMethod() == null ? null : props[i].getReadMethod().invoke(bean));
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {
                    log.error("对象转换为json字符串时出现异常", e);
                }
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    /**
     * 字符串转为Json字符串
     *
     * @param s
     * @return
     */
    public static String stringTojson(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if (ch >= '\u0000' && ch <= '\u001F') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }

    public static Map<String, Object> convertBeanToMap(Object bean) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        Field[] fields2 = bean.getClass().getSuperclass().getDeclaredFields();
        HashMap<String, Object> data = new HashMap<String, Object>();
        for (Field field : fields) {
            field.setAccessible(true);
            data.put(field.getName(), field.get(bean));
        }
        for (Field field : fields2) {
            field.setAccessible(true);
            data.put(field.getName(), field.get(bean));
        }
        return data;
    }

}
