package com.sun.autoconfigure;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Intercepts({@Signature(
    type = Executor.class,
    method = "update",
    args = {MappedStatement.class, Object.class}
), @Signature(
    type = Executor.class,
    method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
    type = Executor.class,
    method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})

public class MybatisAutoSql implements Interceptor {
    Logger logger = LoggerFactory.getLogger("show-sql");

    public MybatisAutoSql() {
    }

    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement)args[0];
        Object parameter = args[1];
        BoundSql boundSql = null;
        String sqlId = ms.getId();
        Configuration configuration = ms.getConfiguration();
        if (args.length > 2) {
            RowBounds rowBounds = (RowBounds)args[2];
            Executor executor = (Executor)invocation.getTarget();
            if (args.length == 4) {
                boundSql = ms.getBoundSql(parameter);
                executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
//                CacheKey cacheKey = (CacheKey)args[4];
                boundSql = (BoundSql)args[5];
            }
        } else {
            boundSql = ms.getBoundSql(parameter);
        }

        long start = System.currentTimeMillis();
        String sql = getSql(configuration, boundSql, sqlId);
        // sql
        logger.info(sqlId + "\n"
                + "  =====>  " + sql);
        Object proceed = invocation.proceed();
        long end = System.currentTimeMillis();
        long time = end - start;
        if (time > 1L) {
            // time
            logger.info(sqlId + "\n"
                    + "  =====>  " + time + "ms");

        }

        return proceed;
    }

    public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sql);
        return str.toString();
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(2, 2, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else if (obj != null) {
            value = obj.toString();
        } else {
            value = "null";
        }

        return value;
    }

    public static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                Iterator var8 = parameterMappings.iterator();

                while(var8.hasNext()) {
                    ParameterMapping parameterMapping = (ParameterMapping)var8.next();
                    String propertyName = parameterMapping.getProperty();
                    Object obj;
                    if (metaObject.hasGetter(propertyName)) {
                        obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }

        return sql;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }
}
