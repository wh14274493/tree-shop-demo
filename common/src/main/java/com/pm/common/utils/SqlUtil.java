package com.pm.common.utils;

import com.pm.common.constants.TableName;
import com.pm.common.domain.TempDomain;
import org.apache.commons.lang.StringUtils;
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
            String fieldName = getSqlColumnName(fields[i].getName());
            sb.append(fieldName);
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
        Field[] allFields = ClassUtil.getAllFields(object);
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
        String fieldName = getSqlColumnName(field.getName());
        StringBuffer sb = new StringBuffer(fieldName + "=");
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
        Field[] allFields = ClassUtil.getAllFields(object);
        Map<String, String> map = generateSetValue(allFields, object);
        sql.SET(map.get("setValue"));
        String condition = generateWhereCondition(Long.parseLong(map.get("id")));
        sql.WHERE(condition);
        return sql;
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

    /**
     * 根据model属性名称生成数据库字段名，例如userName->user_Name
     * @param fieldName
     * @return
     */
    private String getSqlColumnName(String fieldName) {
        char[] fieldNameChar = fieldName.toCharArray();
        StringBuffer index = new StringBuffer();
        //循环遍历得到字符串中所有大写字母的位置，并且以逗号分隔拼接成新的字符串
        for (int i = 0; i < fieldNameChar.length; i++) {
            if (fieldNameChar[i] >= 'A' && fieldNameChar[i] <= 'Z') {
                index.append(i).append(",");
            }
        }
        if (StringUtils.isEmpty(index.toString())) {
            return fieldName;
        }
        String indexStr = index.substring(0,index.length()-1);
        String[] indexChar = indexStr.split(",");
        String[] fieldNameArr = new String[indexChar.length + 1];
        int begin = 0;
        //将model属性字符串按照大写字母所在位置分割成数组
        for (int i = 0; i < indexChar.length; i++) {
            int end = Integer.parseInt(indexChar[i]);
            fieldNameArr[i] = fieldName.substring(begin, end);
            begin = end;
        }
        fieldNameArr[fieldNameArr.length-1] = fieldName.substring(begin, fieldName.length());
        System.out.println(fieldNameArr);
        String columnName = StringUtils.join(fieldNameArr, "_");
        return columnName;
    }

    public static void main(String[] args) {
        TempDomain tempDomain = new TempDomain();
        tempDomain.setAge("18");
        tempDomain.setName("wang");
        tempDomain.setId(123l);
        tempDomain.setCreateTime(DateUtil.getTimestamp());
        tempDomain.setIsDelete(2);
        tempDomain.setUpdateTime(DateUtil.getTimestamp());
        System.out.println(new SqlUtil().generateInsertSql(tempDomain, TableName.USER_TABLE_NAME));
        System.out.println(new SqlUtil().generateUpdateSql(tempDomain, TableName.USER_TABLE_NAME));
    }
}
