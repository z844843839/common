package com.crt.commones.es.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.TypeUtil;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import com.crt.commones.es.pojo.ModelEs;
import com.crt.commones.es.utils.ClassUtil;
import com.crt.commones.es.utils.TemplateUtil;
import org.osgl.$;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ES Crud
 *
 * @author dreamingo
 */
public class EsBaseServiceImp<T extends ModelEs,ID extends Serializable> implements EsBaseSerivce<T,ID> {


    protected EsDao<T> dao;

    protected Class<T> entityClass;

    public EsBaseServiceImp() {
        dao = new EsDao();
        exploreTypes();
        this.entityClass = ClassUtil.getClassGenericType(getClass());
        dao.initEsClient(entityClass);
    }



    @Override
    @Transactional
    public E6Wrapper<List<T>> findListAll() {
        List<T> objList = dao.list("select * from "+dao.indexName());
        return E6WrapperUtil.ok(objList);
    }

    /**
     * 扩展自定义sql
     * @param sql
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper<List<T>> listSql(String sql, Map map) {
        sql = TemplateUtil.processTemplate(sql,map);
        List<T> objList = dao.list(sql);
        return E6WrapperUtil.ok(objList);
    }


    /**
     * 获取满足条件的数量
     *
     * @return 数量
     */
    @Override
    @Transactional
    public E6Wrapper<Long> count() {
        Long ts = dao.count("select count(*) from "+dao.indexName());
        return E6WrapperUtil.ok(ts);
    }


    /**
     * 统计总数 可以自定sql
     * @param sql
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper<Long> countSql(String sql, Map map) {
        sql = TemplateUtil.processTemplate(sql,map);
        Long ts = dao.count(sql);
        return E6WrapperUtil.ok(ts);
    }


    /**
     * 查看：根据ID查找
     * <p>
     * @param id 主键
     * @return 查找到的对象
     */
    @Override
    @Transactional
    public E6Wrapper<T> show(ID id) {
        T obj =  show(id,false);
        if(ObjectUtil.isNull(obj)){
            return E6WrapperUtil.paramError("实体不存在");
        }
        return E6WrapperUtil.ok(obj);
    }


    /**
     * 查看：根据ID查找
     *
     * @param id       主键
     * @param restCall 对象不存在时是否直接返回404
     * @return 查找到的对象
     */
    public T show(ID id, boolean restCall) {
        T obj = dao.findById(id.toString());
        return obj;
    }


    /**
     * 创建
     *
     * @param obj 需创建的对象
     * @return 创建后的对象
     */
    @Override
    @Transactional
    public E6Wrapper<T> create(T obj) {
        obj.objId = UUID.randomUUID().toString();
        String resp = dao.save(obj);
        return  E6WrapperUtil.ok(obj);
    }



    /**
     * 更新
     *
     * @param id  主键
     * @param obj 新对象
     * @return 更新后的对象
     */
    @Override
    @Transactional
    public E6Wrapper<T> modify(ID id, T obj) {
        T o = dao.findById(id.toString());
        if(ObjectUtil.isNull(o)){
            return E6WrapperUtil.paramError("实体不存在");
        }
        $.merge(obj).filter("-id,-v").to(o);
        o.objId = id.toString();
        String resp = dao.save(o);
        return E6WrapperUtil.ok(o);
    }


    /**
     * 删除（物理删除）：根据ID
     * 方法一：直接使用dao根据ID删除
     * dao.deleteById(id);
     *
     * @param id 主键
     */
    @Override
    @Transactional
    public E6Wrapper<T> deleteById(ID id) {
        dao.delete(id.toString());
        return E6WrapperUtil.ok();
    }

    /**
     * 移除（逻辑删除）：根据ID
     *
     * @param id 主键
     */
    @Override
    @Transactional
    public E6Wrapper<T> removeById(ID id) {
        return  stModify( id, 9);
    }
    /**
     * 状态修改
     *
     * @param id 主键
     * @param st 状态
     * @return 是否成功
     */
    protected E6Wrapper stModify(ID id, Integer st) {
        T o = dao.findById(id.toString());
        if (ObjectUtil.isNull(o)) {
            return E6WrapperUtil.paramError("实体不存在");
        }
        o.st = st;
        if (null != o.v) {
            o.v = o.v + 1;
        } else {
            o.v = 1L;
        }
        String resp = dao.save(o);
        return E6WrapperUtil.ok();
    }

    /**
     * 批量移除（逻辑删除）
     *
     * @param i ID List
     * @return 批量移除成功的数量
     */
    public E6Wrapper<T>  removeBatch(List<ID> i) {
        return stModifyBatch((List) i, 9);
    }

    /**
     * 状态批量修改
     *
     * @param i  ID List
     * @param st 状态
     * @return 处理成功的数量
     */
    protected E6Wrapper<T> stModifyBatch(List<ID> i,  Integer st) {
        List<T> objList = dao.findByIdList(i.toArray());
        for (T obj : objList) {
            obj.st = st;
            if (null != obj.v) {
                obj.v = obj.v + 1;
            } else {
                obj.v = 1L;
            }
        }
        String resp = dao.batchUpdate(objList, true);
        return  E6WrapperUtil.ok();
    }

    private void exploreTypes() {
        Type modelType = TypeUtil.getTypeArgument(getClass(), 1);
        Class modelClass = TypeUtil.getClass(modelType);
        dao.setModelClass(modelClass);
    }


}
