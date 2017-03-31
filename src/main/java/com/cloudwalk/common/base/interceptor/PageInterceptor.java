/**
 * Project Name:fundstar-commons
 * File Name:PageInterceptor.java
 * Package Name:com.linc.base.service.interceptor
 * Date:2014年12月18日下午5:15:49
 * Copyright @ 2010-2014 上海企垠信息科技有限公司  All Rights Reserved.
 *
 */

package com.cloudwalk.common.base.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.util.ObjectUtils;

/**
 * ClassName:PageInterceptor <br/>
 * Description: 通过拦截StatementHandler的prepare方法，重写sql语句实现物理分页。 <br/>
 * Date: 2014年12月18日 下午5:15:49 <br/>
 *
 * @author 焦少平
 * @version 1.0
 * @since JDK 1.7
 * @see
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageInterceptor implements Interceptor {

  private static final Logger logger = Logger.getLogger(PageInterceptor.class);
  private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
  private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
  private static String defaultDialect = "oracle"; // 数据库类型(默认为oracle)
  private static String defaultPageSqlId = ".*Page$"; // 需要拦截的ID(正则匹配)
  private static String dialect = ""; // 数据库类型(默认为oracle)
  private static String pageSqlId = ""; // 需要拦截的ID(正则匹配)

  public static String getDialect() {
	return dialect;
	}
	
	public static void setDialect(String dialect) {
		PageInterceptor.dialect = dialect;
	}

/**
   * buildPageSql:拼装sql语句 <br/>
   * 适用条件：支持mysql和oracle数据库<br/>
   * 执行流程：内部调用<br/>
   *
   * @author:焦少平 Date: 2014年12月19日 上午10:32:00
   * @param sql sql语句
   * @param page 分页信息
   * @return String 拼装过后的sql语句
   * @since JDK 1.7
   */
  private String buildPageSql(String sql, PageInfo page) {
    if (page != null) {
      StringBuilder pageSql = new StringBuilder();
      if ("mysql".equals(dialect)) {
        //调用mysql的拼装方法
        pageSql = buildPageSqlForMysql(sql, page);
      } else if ("oracle".equals(dialect)) {
        //调用oracle的拼装方法
        pageSql = buildPageSqlForOracle(sql, page);
      } else if ("db2".equals(dialect)) {
    	pageSql = buildPageSqlForDB2(sql, page);
      } else {
        return sql;
      }
      return pageSql.toString();
    } else {
      return sql;
    }
  }

  /**
   * buildPageSqlForMysql:mysql的分页语句 <br/>
   * 适用条件：mysql数据库<br/>
   * 执行流程：内部调用<br/>
   *
   * @author:焦少平 Date: 2014年12月19日 上午10:37:22
   * @param sql sql语句
   * @param page 分页信息
   * @return StringBuilder 拼装过后的sql语句
   * @since JDK 1.7
   */
  public StringBuilder buildPageSqlForMysql(String sql, PageInfo page) {
    StringBuilder pageSql = new StringBuilder(100);
    //计算起始行
    String beginrow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
    pageSql.append(sql);
    pageSql.append(" limit " + beginrow + "," + page.getPageSize());
    return pageSql;
  }

  /**
   * buildPageSqlForOracle:oracle分页方法 <br/>
   * 适用条件：oracle数据库<br/>
   * 执行流程：内部调用<br/>
   *
   * @author:焦少平 Date: 2014年12月19日 上午10:49:43
   * @param sql sql语句
   * @param page 分页信息
   * @return StringBuilder 拼装过后的sql语句
   * @since JDK 1.7
   */
  public StringBuilder buildPageSqlForOracle(String sql, PageInfo page) {
    StringBuilder pageSql = new StringBuilder(100);
    //计算起始行
    String beginrow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
    //计算截止行
    String endrow = String.valueOf(page.getCurrentPage() * page.getPageSize());
    pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
    pageSql.append(sql);
    pageSql.append(" ) temp where rownum <= ").append(endrow);
    pageSql.append(") where row_id > ").append(beginrow);
    return pageSql;
  }
  
  /**
   * buildPageSqlForDB2:DB2分页方法 <br/>
   * 适用条件：DB2数据库<br/>
   * 执行流程：内部调用<br/>
   *
   * @author:zhuyunfei Date: 2016年08月31日 上午15:49:43
   * @param sql sql语句
   * @param page 分页信息
   * @return StringBuilder 拼装过后的sql语句
   * @since JDK 1.7
   */
  public StringBuilder buildPageSqlForDB2(String sql, PageInfo page) {
    StringBuilder pageSql = new StringBuilder(100);
    //计算起始行
    String beginrow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
    //计算截止行
    String endrow = String.valueOf(page.getCurrentPage() * page.getPageSize());
    pageSql.append("select * from (select a.*,rownumber() over() as rowid from (");
    pageSql.append(sql);
    pageSql.append(") a) tmp where tmp.rowid > ").append(beginrow);
    pageSql.append(" and tmp.rowid <= ").append(endrow);
    return pageSql;
  }

  /**
   * 重写拦截方法
   *
   * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)
   */
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,
        DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
    while (metaStatementHandler.hasGetter("h")) {
      Object object = metaStatementHandler.getValue("h");
      metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
          DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
    // 分离最后一个代理对象的目标类
    while (metaStatementHandler.hasGetter("target")) {
      Object object = metaStatementHandler.getValue("target");
      metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
          DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
    Configuration configuration = (Configuration) metaStatementHandler
        .getValue("delegate.configuration");
    dialect = configuration.getVariables().getProperty("dialect");
    if (null == dialect || "".equals(dialect)) {
      logger.warn("Property dialect is not setted,use default 'oracle' ");
      dialect = defaultDialect;
    }
    pageSqlId = configuration.getVariables().getProperty("pageSqlId");
    if (null == pageSqlId || "".equals(pageSqlId)) {
      logger.warn("Property pageSqlId is not setted,use default '.*Page$' ");
      pageSqlId = defaultPageSqlId;
    }
    MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
        .getValue("delegate.mappedStatement");
    // 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
    if (mappedStatement.getId().matches(pageSqlId)) {
      BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
      Object parameterObject = boundSql.getParameterObject();
      if (parameterObject == null) {
        throw new NullPointerException("parameterObject is null!");
      } else {
        PageInfo page = (PageInfo) metaStatementHandler
            .getValue("delegate.boundSql.parameterObject.page");
        String sql = boundSql.getSql();
        // 重写sql
        String pageSql = buildPageSql(sql, page);
        metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
        // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        Connection connection = (Connection) invocation.getArgs()[0];
        // 重设分页参数里的总页数等
        setPageParameter(sql, connection, mappedStatement, boundSql, page);
      }
    }
    // 将执行权交给下一个拦截器
    return invocation.proceed();
  }
   

  /**
   * 检测目标是不是StatementHandler类型，如果是，则包装该类，不是，直接返回目标本身
   *
   * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
   */
  @Override
  public Object plugin(Object target) {
    // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
    if (target instanceof StatementHandler) {
      return Plugin.wrap(target, this);
    } else {
      return target;
    }
  }

  /**
   * setPageParameter:从数据库里查询总的记录数并计算总页数，回写进分页参数PageParameter,<br>
   * 适用条件：内部调用<br/>
   *
   * @author:焦少平 Date: 2014年12月19日 上午11:11:44
   * @param sql sql语句
   * @param connection 数据库连接对象
   * @param mappedStatement 映射文件对象
   * @param boundSql 实际执行的sql对象
   * @param page 分页对象
   * @since JDK 1.7
   */
  private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
      BoundSql boundSql, PageInfo page) {
    // 记录总记录数
    String countSql = "select count(0) as total from (" + sql + ") temp";
    PreparedStatement countStmt = null;
    ResultSet rs = null;
    try {
      countStmt = connection.prepareStatement(countSql);
      BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
          boundSql.getParameterMappings(), boundSql.getParameterObject());
      
      //通过反射获取metaParameters对象，并将该对象设置到countBS中
      MetaObject metaParameters = (MetaObject)ObjectUtils.getFieldValue(boundSql, "metaParameters");
      ObjectUtils.setFieldValue(countBS, "metaParameters", metaParameters);      
      
      //对SQL参数设值
      setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
      //执行结果
      rs = countStmt.executeQuery();
      int totalCount = 0;
      if (rs.next()) {
        //得到总记录数
        totalCount = rs.getInt(1);
      }
      page.setTotalCount(totalCount);
      //计算总页数
      int totalPage = totalCount / page.getPageSize()
          + ((totalCount % page.getPageSize() == 0) ? 0 : 1);
      page.setTotalPage(totalPage);

    } catch (SQLException e) {
      logger.error("Query to the total number of records in an error", e);
    } finally {
      try {
        rs.close();
      } catch (SQLException e) {
        logger.error("An error has occurred close the result set", e);
      }
      try {
        countStmt.close();
      } catch (SQLException e) {
        logger.error("An error has occurred close the PreparedStatement", e);
      }
    }

  }

  /**
   * setParameters:对SQL参数(?)设值 <br/>
   * 适用条件：内部调用<br/>
   *
   * @author:焦少平 Date: 2014年12月19日 上午11:31:16
   * @param ps 预处理对象
   * @param mappedStatement 映射文件对象
   * @param boundSql sql语句实际执行的对象
   * @param parameterObject 分页对象
   * @throws SQLException 数据库异常
   * @since JDK 1.7
   */
  private void setParameters(PreparedStatement ps, MappedStatement mappedStatement,
      BoundSql boundSql, Object parameterObject) throws SQLException {
    ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
        parameterObject, boundSql);
    parameterHandler.setParameters(ps);
  }

  /**
   * 实现父类方法
   *
   * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
   */
  @Override
  public void setProperties(Properties properties) {
  }

}
