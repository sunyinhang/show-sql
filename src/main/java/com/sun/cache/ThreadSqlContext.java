package com.sun.cache;


import com.sun.entity.SqlCache;
import com.sun.enums.SqlType;

import java.util.ArrayList;

/**
 * @Author sun
 * @Date 2020/10/14
 * @Desc
 **/
public final class ThreadSqlContext {

    /**
     * 线程本地存储
     */

    private static final ThreadLocal<SqlCache> sqlCache = InheritThreadLocal.withInitial(SqlCache::new);

    private static final ArrayList<String> tablesBlacklist = new ArrayList<String>() {{
        add("kss_zyry");
    }};

    // sql缓存开关
    private static volatile boolean cacheFlag = false;

    public static void setCacheFlag(boolean flag) {
        cacheFlag = flag;
    }

    public static void addSql(String sqlStr) {

        // 判断开关
        if (!cacheFlag) {
            return;
        }

        String lowerCase = sqlStr.toLowerCase();
        SqlType sqlType = SqlType.getType(lowerCase);
        // 验证sql类型是否需要同步
        if (!sqlType.sync()) {
            return;
        }

        String tableName = sqlType.getTableName(lowerCase);
        // 如果包含在黑名单, 直接返回
        if (tablesBlacklist.contains(tableName)) {
            return;
        }

        // 添加到缓存
        sqlCache.get().addSql(lowerCase, sqlType.name(), tableName);
    }

//    public static void setEnd(boolean end) {
//        sqlCache.get().setEnd(end);
//    }

    public static void deleteCache() {
        sqlCache.remove();
    }

    public static SqlCache get() {
        return sqlCache.get();

    }
}
