package com.crt.common.sqlInterceptor;


import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.crt.common.constant.Constants;
import com.crt.common.util.UserInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * 数据权限拦截
 *
 * @author malin
 * @date 2019/09/21
 */

@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataAuthInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String rowSql = UserInfoUtil.getRowDataAuthSQL();
        if (StringUtils.isNotEmpty(rowSql)){
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
            //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            //id为执行的mapper方法的全路径名，如com.uv.dao.UserMapper.insertUser
            String id = mappedStatement.getId();
            //sql语句类型 select、delete、insert、update
            String sqlCommandType = mappedStatement.getSqlCommandType().toString();
            BoundSql boundSql = statementHandler.getBoundSql();

            //获取到原始sql语句
            String sql = boundSql.getSql();
            String mSql = sql;
            //TODO 修改位置

            //注解逻辑判断  添加注解了才拦截
            Class<?> classType = Class.forName(mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf(".")));
            String mName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1, mappedStatement.getId().length());
            for (Method method : classType.getDeclaredMethods()) {
                if (method.isAnnotationPresent(InterceptAnnotation.class) && mName.equals(method.getName())) {
                    InterceptAnnotation interceptorAnnotation = method.getAnnotation(InterceptAnnotation.class);
                    boolean totalFlag = false;
                    String tableAlias = null;
                    String countStart = "SELECT count(1)\n FROM (";
                    String countEnd = ") total";
    //                sql = cleanSqlSpace(sql);
                    /**
                     * SQL格式化
                     * @param sql 查询sql语句
                     * @param 数据库类型
                     * @param 关键字大写
                     */
                    sql = SQLUtils.format(sql,JdbcConstants.MYSQL,SQLUtils.DEFAULT_FORMAT_OPTION);
                    if (interceptorAnnotation.queryType() == InterceptAnnotation.QueryAuthSqlType.QUERY_COUNT) {
                        sql = sql.substring(sql.indexOf(countStart) + countStart.length(), sql.length());
                        sql = sql.substring(0, sql.indexOf(countEnd));
                        totalFlag = true;
                    }
                    tableAlias = sql.substring(sql.indexOf("SELECT") + 7, sql.indexOf("."));
    //                String rowSql = UserInfoUtil.getRowDataAuthSQL(null, tableAlias);
                    if (interceptorAnnotation.flag()) {
                        mSql = interceptSQL(sql, tableAlias, rowSql);
                        if (totalFlag) {
                            StringBuffer sb = new StringBuffer();
                            sb.append(countStart).append(Constants.SPACE);
                            sb.append(mSql);
                            sb.append(Constants.SPACE).append(countEnd);
                            mSql = sb.toString();
                        }
                    }
                }
            }

            //通过反射修改sql语句
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, mSql);
        }
        return invocation.proceed();
    }

    /**
     * 清除sql中多余空格
     * 保持每个单词之间最多有一个空格
     *
     * @param sql
     * @return String
     */
    private String cleanSqlSpace(String sql) {
        StringBuilder result = new StringBuilder();
        // 前一个是否为空格，默认第一个不是
        boolean space = false;
        for (int i = 0; i < sql.length(); i++) {
            if (!Constants.SPACE.equals(sql.charAt(i))) {
                //当前不是空格
                space = false;
                result.append(sql.charAt(i));
            } else if (!space) {
                //当前是空格，但前一个不是空格
                space = true;
                result.append(sql.charAt(i));
            }
        }
        return result.toString();
    }

    /**
     * sql拼接数据权限条件
     *
     * @param sql
     * @param alias
     * @param rowSql
     * @return String
     */
    private static String interceptSQL(String sql, String alias, String rowSql) {
        String cutPoint = "";
        String noWhereSqlOne = "";
        String noWhereSqlTwo = "";
        if (sql.contains(Constants.RIGHT_PARENTHESES + Constants.SPACE + alias)){
            cutPoint = Constants.RIGHT_PARENTHESES + Constants.SPACE + alias.trim();
            noWhereSqlOne = Constants.RIGHT_PARENTHESES + Constants.SPACE + alias + "\n\t" + Constants.SQL_KEY_WHERE;
            noWhereSqlTwo = Constants.RIGHT_PARENTHESES + Constants.SPACE + alias + "\n" + Constants.SQL_KEY_WHERE;
        }else if(sql.contains(Constants.SQL_KEY_WHERE)) {
            cutPoint = Constants.SQL_KEY_WHERE;
            noWhereSqlOne = Constants.SPACE + alias + "\n\t" + Constants.SQL_KEY_WHERE;
            noWhereSqlTwo = Constants.SPACE + alias + "\n" + Constants.SQL_KEY_WHERE;
        }else {
            List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
            SQLStatement stmt = stmtList.get(0);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
            //获取表名称
            String tableName = visitor.getCurrentTable();
            cutPoint = Constants.SQL_KEY_FROM + Constants.SPACE + tableName + Constants.SPACE + alias;
        }
        String moreThan = sql.substring(sql.lastIndexOf(cutPoint) + (cutPoint.length()), sql.length()).trim();
        sql = sql.substring(0, sql.lastIndexOf(cutPoint) + (cutPoint.length()));
        if (moreThan.indexOf(Constants.SQL_KEY_WHERE) >= Constants.NUMBER_ZERO) {
            moreThan = moreThan.substring(moreThan.indexOf(Constants.SQL_KEY_WHERE) + Constants.NUMBER_FIVE, moreThan.length()).trim();
        }
        if ((StringUtils.isNotEmpty(noWhereSqlOne) || StringUtils.isNotEmpty(noWhereSqlTwo)) && (sql.contains(noWhereSqlOne) || sql.contains(noWhereSqlTwo))){
            sql = sql.substring(0,sql.lastIndexOf(Constants.SQL_KEY_WHERE));
        }
        if (moreThan.indexOf(alias.trim() + Constants.SPOT) >= Constants.NUMBER_ZERO) {
            if (moreThan.indexOf(Constants.SQL_KEY_ORDER_BY) == Constants.NUMBER_ZERO || moreThan.indexOf(Constants.SQL_KEY_GROUP_BY) == Constants.NUMBER_ZERO){
                moreThan = moreThan.trim();
            }else {
                moreThan = Constants.CONNECTOR_AND + Constants.SPACE + moreThan.trim();
            }
        }
        StringBuffer sb = new StringBuffer(sql);
        sb.append(Constants.SPACE).append(Constants.SQL_KEY_WHERE);
        sql = sb.toString();
        String finalSql = sql + Constants.SPACE + rowSql + Constants.SPACE + moreThan;
        return finalSql;
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }

    }

    @Override
    public void setProperties(Properties properties) {

    }
}
