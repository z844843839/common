package com.crt.common.i18n;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.util.locale.LanguageTag;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/1/30 16:41
 * @Description 国际化加载，从数据库，外部可以指定 数据源，查询SQL及主键列名称
 **/
public class DefaultE6I18nDataLoadServiceImpl implements E6I18nDataLoadService {
    static Logger logger = LoggerFactory.getLogger(DefaultE6I18nDataLoadServiceImpl.class);

    /**
     * 数据源
     */
    DataSource dataSource;
    /**
     * 默认的查询SQL
     */
    final static String  DEFAULT_SQL = "SELECT * FROM i18n_message";
    /**
     * 默认的主键列名称
     */
    final static String  DEFAULT_COLUMN_NAME_CODE = "code";

    String sql = DEFAULT_SQL;
    String columnNameCode = DEFAULT_COLUMN_NAME_CODE;

    @Override
    public List<E6I18nDictByLocaleEntity> loadE6I18nDictByLocaleEntity() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Map<Locale,Map<String,String>> localeKvMap = new HashMap<>();
        jdbcTemplate.query(sql, (ResultSet resultSet) -> {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int colCount = resultSetMetaData.getColumnCount();
            Map<String,String>[] kvMapArray = new HashMap[colCount-1];
            if(!columnNameCode.equals(resultSetMetaData.getColumnName(1).toLowerCase())){
                logger.error("i18n_message 表格中第一列的列名称必须是code或CODE");
                throw new SQLException("i18n_message 表格中第一列的列名称必须是code或CODE");
            }
            //数据库表中的字段不区分大小写，如果只有语言，应该是两个字符，如果是语言加国家，应该是语言-国家，有个中划线
            for(int i=2;i<=colCount;i++){
                String columnNameOfLang = resultSetMetaData.getColumnName(i).toLowerCase();
                Map<String,String>  kvMap = new HashMap<String,String>();
                kvMapArray[i-2]=kvMap;
                //切分字符串，拼装为Locale
                LanguageTag languageTag = LanguageTag.parse(columnNameOfLang, null);
                Locale locale = new Locale(languageTag.getLanguage(),languageTag.getRegion());
                logger.info("i18n_message中列名{}解析为语言类型为:{},({})",i,locale.toString(),locale.getDisplayName());
                localeKvMap.put(locale,kvMap);
            }
            do{
                String code = resultSet.getString(1);
                for(int i=2;i<=colCount;i++){
                    kvMapArray[i-2].put(code,resultSet.getString(i));
                }
            }while(resultSet.next());
            logger.info(JSONObject.toJSONString(localeKvMap));
        });
        List<E6I18nDictByLocaleEntity> e6I18nDictByLocaleEntities = new ArrayList<>();
        for (Map.Entry<Locale,Map<String,String>> entry:localeKvMap.entrySet()){
            E6I18nDictByLocaleEntity e6I18nDictByLocaleEntity = new E6I18nDictByLocaleEntity();
            e6I18nDictByLocaleEntity.setLocale(entry.getKey());
            e6I18nDictByLocaleEntity.setE6I18nDictKVMap(entry.getValue());
            e6I18nDictByLocaleEntities.add(e6I18nDictByLocaleEntity);
        }
        return e6I18nDictByLocaleEntities;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setColumnNameCode(String columnNameCode) {
        this.columnNameCode = columnNameCode;
    }

}
