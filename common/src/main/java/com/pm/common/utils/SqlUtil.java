package com.pm.common.utils;

import com.pm.common.constants.TableName;
import com.pm.common.domain.TempDomain;
import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class SqlUtil {

    /**
     * 增加
     *
     * @param paramMap
     * @return
     */
    public String insert(Map paramMap) {
        Object object = paramMap.get("obj");
        String table = (String) paramMap.get("table");
        return generateInsertSql(object, table).toString();
    }

    /**
     * 删除
     *
     * @param paramMap
     * @return
     */
    public String delete(Map paramMap) {
        Long id = (Long) paramMap.get("id");
        String table = (String) paramMap.get("table");
        return generateDeleteSql(id, table).toString();
    }

    /**
     * 修改
     *
     * @param paramMap
     * @return
     */
    public String update(Map paramMap) {
        Object object = paramMap.get("obj");
        String table = (String) paramMap.get("table");
        return generateUpdateSql(object, table).toString();
    }

    /**
     * 根据id进行查找
     *
     * @param paramMap
     * @return
     */
    public String findById(Map paramMap) {
        Long id = (Long) paramMap.get("id");
        String table = (String) paramMap.get("table");
        return generateSelectSql(id, table).toString();
    }

    /**
     * 查询所有
     *
     * @param paramMap
     * @return
     */
    public String findAll(Map paramMap) {
        String table = (String) paramMap.get("table");
        return generateSelectAllSql(table).toString();
    }

    /**
     * 生成sql语句的column字符串
     *
     * @param fields 实体类及其父类的属性名称
     * @return
     */
    private String generateSqlColumn(Field[] fields) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i].getName());
            if (i < fields.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * 生成sql语句的values字符串
     *
     * @param fields 实体类及其父类的属性名称
     * @return
     */
    private String generateSqlValue(Field[] fields, Object object) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                field.setAccessible(true);
                Object value = fields[i].get(object);
                if (value != null && (value instanceof String || value instanceof Timestamp)) {
                    sb.append("'" + value + "'");
                } else {
                    sb.append(value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (i < fields.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * 生成插入的sql语句
     *
     * @param object 需要插入的对象
     * @param table  对应的数据库表名
     * @return
     */
    private SQL generateInsertSql(Object object, String table) {
        Field[] allFields = getAllFields(object);
        String columns = generateSqlColumn(allFields);
        String values = generateSqlValue(allFields, object);
        SQL sql = new SQL();
        //设置表名
        sql.INSERT_INTO(table);
        //设置column和values
        sql.VALUES(columns, values);
        return sql;
    }

    /**
     * 生成where条件语句
     *
     * @param id
     * @return
     */
    private String generateWhereCondition(Long id) {
        StringBuffer sb = new StringBuffer("id=");
        sb.append(id);
        return sb.toString();
    }

    /**
     * 生成where条件语句
     *
     * @param object
     * @return
     */
    private String generateWhereCondition(Field field, Object object) {
        StringBuffer sb = new StringBuffer(field.getName() + "=");
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (value != null && (value instanceof String || value instanceof Timestamp)) {
            sb.append("'" + value + "'");
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 生成删除的sql语句
     *
     * @param id    需要删除记录的id
     * @param table 表名
     * @return
     */
    private SQL generateDeleteSql(Long id, String table) {
        SQL sql = new SQL();
        sql.DELETE_FROM(table);
        String condition = generateWhereCondition(id);
        sql.WHERE(condition);
        return sql;
    }

    private Map<String, String> generateSetValue(Field[] allFields, Object object) {
        StringBuffer sb = new StringBuffer();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < allFields.length; i++) {
            if ("id".equals(allFields[i].getName())) {
                try {
                    allFields[i].setAccessible(true);
                    map.put("id", String.valueOf(allFields[i].get(object)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            sb.append(generateWhereCondition(allFields[i], object));
            if (i < allFields.length - 1) {
                sb.append(",");
            }
        }
        map.put("setValue", sb.toString());
        return map;
    }

    /**
     * 生成更新的sql语句
     *
     * @param object 需要更新的对象
     * @param table  表名
     * @return
     */
    private SQL generateUpdateSql(Object object, String table) {
        SQL sql = new SQL();
        sql.UPDATE(table);
        Field[] allFields = getAllFields(object);
        Map<String, String> map = generateSetValue(allFields, object);
        sql.SET(map.get("setValue"));
        String condition = generateWhereCondition(Long.parseLong(map.get("id")));
        sql.WHERE(condition);
        return sql;
    }

    /**
     * 根据对象获取所有的Field数组（包含继承的Field）
     *
     * @param object
     * @return
     */
    public Field[] getAllFields(Object object) {
        Class<?> cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        Field[] superFields = cls.getSuperclass().getDeclaredFields();
        //数组合并
        Field[] allFields = (Field[]) ArrayUtils.addAll(fields, superFields);
        return allFields;
    }

    /**
     * 生成单个查询的sql语句
     *
     * @param id
     * @param table
     * @return
     */
    private SQL generateSelectSql(Long id, String table) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(table);
        String condition = generateWhereCondition(id);
        sql.WHERE(condition);
        return sql;
    }

    /**
     * 生成查询所有的sql语句
     *
     * @param table
     * @return
     */
    private SQL generateSelectAllSql(String table) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(table);
        return sql;
    }

}
