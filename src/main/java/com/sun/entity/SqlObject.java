package com.sun.entity;

public class SqlObject {
    private String sqlType;     // sql类型, insert, update, delete, select
    private String tableName;
    private String sqlStr;

    public SqlObject(String sqlStr, String sqlType, String tableName) {
        this.sqlStr = sqlStr;
        this.sqlType = sqlType;
        this.tableName = tableName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }


}