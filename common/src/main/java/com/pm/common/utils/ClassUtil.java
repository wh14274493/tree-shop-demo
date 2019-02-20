package com.pm.common.utils;

import org.apache.commons.lang.ArrayUtils;
import java.lang.reflect.Field;

public class ClassUtil {

    /**
     * 合并两个对象的属性（若属性值不为null则以newObject为准，否则不做修改）
     *
     * @param newObject
     * @param oldObject
     * @param <T>
     * @return
     */
    public static <T> T combine(T newObject, T oldObject) {
        Field[] newFields = getAllFields(newObject);
        Field[] oldFields = getAllFields(oldObject);
        for (int i = 0; i < newFields.length; i++) {
            newFields[i].setAccessible(true);
            try {
                Object value = newFields[i].get(newObject);
                if (value != null) {
                    oldFields[i].setAccessible(true);
                    oldFields[i].set(oldObject, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return oldObject;
    }

    /**
     * 根据对象获取所有的Field数组（包含继承的Field）
     *
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        Class<?> cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        Field[] superFields = cls.getSuperclass().getDeclaredFields();
        //数组合并
        Field[] allFields = (Field[]) ArrayUtils.addAll(fields, superFields);
        return allFields;
    }
}
