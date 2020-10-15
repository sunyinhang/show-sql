package com.sun.entity;

import java.util.ArrayList;

public class SqlCache {

    private String threadName;  // 线程名称
    //    private boolean end;        // 是否正常结束
    private ArrayList<SqlObject> sqlList;     // 执行过的sql列表



    public SqlCache() {
        this.threadName = Thread.currentThread().getName();
        this.sqlList = new ArrayList<>();
    }



    public String getThreadName() {
        return threadName;
    }

    public ArrayList<SqlObject> getSqlList() {
        return sqlList;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void setSqlList(ArrayList<SqlObject> sqlList) {
        this.sqlList = sqlList;
    }

    public void addSql(String sqlStr, String sqlType, String tableName) {
        sqlList.add(new SqlObject(sqlStr, sqlType, tableName));
    }

}
