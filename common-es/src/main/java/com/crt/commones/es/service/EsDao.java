package com.crt.commones.es.service;

import cn.hutool.core.collection.CollUtil;
import com.crt.commones.es.annotation.EsIndex;
import com.crt.commones.es.utils.EsConfigUtil;
import org.frameworkset.elasticsearch.ElasticSearchException;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.RestResponse;
import org.frameworkset.elasticsearch.handler.ElasticSearchResponseHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * ElasticSearch Dao
 *
 * @author masibin
 */
@Component
public class EsDao<T> {

    protected Class<T> modelClass;

    protected ClientInterface esClient;

    public EsDao() {
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }

    public Class<T> modelType() {
        return modelClass;
    }

    public void initEsClient(Class<T>  modelClass) {
        this.modelClass = modelClass;
        esClient = ElasticSearchHelper.getRestClientUtil();
        try {
            //判读索引表是否存在，存在返回true，不存在返回false
            boolean exist = esClient.existIndice(indexName());
            //如果索引表demo已经存在先删除mapping
            if (!exist) {
                String config = EsConfigUtil.buildEsConfig(modelClass);
                //创建索引表demo //索引表名称 //索引表mapping dsl脚本名称，在esmapper/{config}.xml中定义createIndice
                esClient.createIndiceMapping(indexName(), config);

            }
        } catch (ElasticSearchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行es-sql插件 sql语句，获取对象
     *
     * @param ql 查询语句
     * @return 对象
     */
    public T findOne(String ql) {
        T obj = esClient.searchObject("/_sql", ql, modelClass);
        return obj;
    }

    /**
     * 执行es-sql插件 sql语句，获取对象列表
     *
     * @param ql 查询语句
     * @return 对象列表
     */
    public List<T> list(String ql) {
        ESDatas<T> objList = esClient.searchList("/_sql", ql, modelClass);
        return objList.getDatas();
    }

    /**
     * 执行es-sql插件 sql语句，获取Map列表
     *
     * @param ql 查询语句
     * @return Map列表
     */
    public List<Map> listMap(String ql) {
        ESDatas<Map> objList = esClient.searchList("/_sql", ql, Map.class);
        return objList.getDatas();
    }

    //TODO: 实现多字段group
    public List<Map<String, Object>> listAggregationBuckets(String ql, String group) {
        ESDatas<Map> esDatas = esClient.searchList("/_sql", ql, Map.class);
        List<Map<String, Object>> buckets = esDatas.getAggregationBuckets(group);
        if (CollUtil.isNotEmpty(buckets)) {
            for (Map<String, Object> b : buckets) {
                if (CollUtil.isNotEmpty(b)) {
                    for (Map.Entry entry : b.entrySet()) {
                        if (null == entry || null == entry.getKey()) {
                            continue;
                        }
                        if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                            entry.setValue(((Map) entry.getValue()).get("value"));
                        }
                    }
                }
            }
        }

        return buckets;
    }

    public List<Map> sqlAggregationBuckets(String ql) {
        List<Map> objList = esClient.sql(Map.class, "{\"query\":\"" + ql + "\"}");
        return objList;
    }

    /**
     * 执行es sql语句，获取对象列表
     *
     * @param ql 查询语句
     * @return 对象列表
     */
    public List<T> sql(String ql) {
        List<T> objList = esClient.sql(modelClass, "{\"query\":\"" + ql + "\"}");
        return objList;
    }

    /**
     * 执行es sql语句，获取Map列表
     *
     * @param ql 查询语句
     * @return Map列表
     */
    public List<Map> sqlMap(String ql) {
        List<Map> objList = esClient.sql(Map.class, "{\"query\":\"" + ql + "\"}");
        return objList;
    }

    public Map<String, Map<String, Object>> sqlAggregations(String ql) {
        RestResponse result = esClient.executeRequest("/_sql", ql, new ElasticSearchResponseHandler(Map.class));
        Map<String, Map<String, Object>> map = result.getAggregations();
        return map;
    }

    /**
     * 统计数量
     *
     * @param ql 统计语句
     * @return 数量
     */
    public Long count(String ql) {

        long ts = 0;
        Map<String, Map<String, Object>> map = sqlAggregations(ql);
        if (CollUtil.isNotEmpty(map)) {
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                Map<String, Object> im = entry.getValue();
                if (CollUtil.isNotEmpty(im)) {
                    ts = Long.decode(im.get("value").toString());
                }
                break;
            }
        }
        return ts;
    }

    /**
     * 根据ID查找对象
     *
     * @param id ID
     * @return 对象
     */
    public T findById(String id) {
        T obj = (T) esClient.getDocument(indexName(), indexName(), id, modelClass);
        return obj;
    }

    /**
     * 根据ID集合批量获取文档对象
     *
     * @param i ID集合
     * @return 文档对象
     */
    public List<T> findByIdList(Object[] i) {
        List<T> objList = esClient.mgetDocuments(indexName(), indexName(), modelClass, i);
        return objList;
    }

    /**
     * 保存文档对象
     *
     * @param obj 文档对象
     * @return 响应JSON
     */
    public String save(T obj) {
        return save(obj, true);
    }

    /**
     * 保存文档对象
     *
     * @param obj 文档对象
     * @return 响应JSON
     */
    public String save(T obj, boolean refresh) {
        String resp = "";
        if (refresh) {
            resp = esClient.addDocument(indexName(), indexName(), obj, "refresh");
        } else {
            resp = esClient.addDocument(indexName(), indexName(), obj);
        }
        return resp;
    }

    /**
     * 批量保存对象
     *
     * @param objList 对象列表
     * @return 响应JSON
     */
    public String batchSave(List<T> objList) {
        return batchSave(objList, true);
    }

    /**
     * 批量保存对象
     *
     * @param objList 对象列表
     * @param refresh 是否刷新
     * @return 响应JSON
     */
    public String batchSave(List<T> objList, boolean refresh) {
        String resp = "";
        if (refresh) {
            //refresh=true
            resp = esClient.addDocuments(indexName(), indexName(), objList, "refresh");
        } else {
            resp = esClient.addDocuments(indexName(), indexName(), objList);
        }
        return resp;
    }

    /**
     * 批量更新对象
     *
     * @param objList 对象列表
     * @return 响应JSON
     */
    public String batchUpdate(List<T> objList) {
        return batchUpdate(objList, true);
    }

    /**
     * 批量更新对象
     *
     * @param objList 对象列表
     * @param refresh 是否刷新
     * @return 响应JSON
     */
    public String batchUpdate(List<T> objList, Boolean refresh) {
        String resp = "";
        if (refresh) {
            resp = esClient.updateDocuments(indexName(), indexName(), objList, "refresh");
        } else {
            resp = esClient.updateDocuments(indexName(), indexName(), objList);
        }
        return resp;
    }

    /**
     * 根据ID删除文档
     *
     * @param id ID
     * @return 响应JSON
     */
    public String delete(String id) {
        String resp = esClient.deleteDocument(indexName(), indexName(), id);
        return resp;
    }

    /**
     * 根据ID批量删除
     *
     * @param i IDList
     * @return 响应JSON
     */
    public String batchDelete(String... i) {
        String resp = esClient.deleteDocuments(indexName(), indexName(), i);
        return resp;
    }

    public String indexName() {
        EsIndex index = modelClass.getAnnotation(EsIndex.class);
        if (null != index) {
            return index.name();
        }
        return null;
    }

    public String indexConfig() {
        EsIndex index = modelClass.getAnnotation(EsIndex.class);
        if (null != index) {
            return index.config();
        }
        return null;
    }

}
