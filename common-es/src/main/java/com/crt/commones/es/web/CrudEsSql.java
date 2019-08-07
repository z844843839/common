package com.crt.commones.es.web;

import com.crt.common.vo.E6Wrapper;
import com.crt.commones.es.pojo.ModelEs;
import com.crt.commones.es.service.EsBaseSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * ES Crud
 *
 * @author masibin
 */
public abstract class CrudEsSql<T extends ModelEs,ID extends Serializable> {

    @Autowired
    EsBaseSerivce baseSerivce;

    protected static final Logger logger = LoggerFactory.getLogger(CrudEsSql.class);

    /**
     * 列表：根据条件查找
     *
     * @return 查找到的对象列表
     */
    @PostMapping()
    public E6Wrapper<List<T>> list() {
        E6Wrapper<List<T>> objList = baseSerivce.findListAll();
        return objList;
    }

    /**
     * 扩展自定义sql
     * @param sql
     * @return
     */
    public E6Wrapper<List<T>> listPlus(String sql, Map map) {
        return  baseSerivce.listSql(sql,map);
    }
    /**
     * 获取满足条件的数量
     *
     * @return 数量
     */
    @PostMapping("count")
    @Transactional
    public E6Wrapper<Long> count() {
        return baseSerivce.count();
    }

    /**
     * 可以自定sql
     * @param sql
     * @return
     */
    public E6Wrapper<Long> countPlus(String sql,Map map) {
        return baseSerivce.countSql(sql,map);
    }

    /**
     * 查看：根据ID查找
     * <p>
     * ACT的自动ID绑定方式：(@DbBind("id") T obj)
     *
     * @param id 主键
     * @return 查找到的对象
     */
    @PostMapping("show/{id}")
    @Transactional
    public E6Wrapper<T> show(@NotNull @PathVariable ID id) {
        return baseSerivce.show(id);
    }


    /**
     * 创建
     *
     * @param obj 需创建的对象
     * @return 创建后的对象
     */
    @PostMapping("create")
    @Transactional
    public E6Wrapper<T> create(@RequestBody T obj) {
        return  baseSerivce.create(obj);

    }

    /**
     * 更新
     *
     * @param id  主键
     * @param obj 新对象
     * @return 更新后的对象
     */
    @PostMapping("update/{id}")
    @Transactional
    public E6Wrapper<T> update(@NotNull @PathVariable ID id, @RequestBody T obj) {
        return  baseSerivce.modify(id,obj);
    }


    /**
     * 删除（物理删除）：根据ID
     * 方法一：直接使用dao根据ID删除
     * dao.deleteById(id);
     *
     * @param id 主键
     */
    @PostMapping("delete/{id}")
    @Transactional
    public E6Wrapper<T> deleteById(@NotNull @PathVariable ID id) {
        return  baseSerivce.deleteById(id);
    }


    /**
     * 移除（逻辑删除）：根据ID
     *
     * @param id 主键
     */
    @PostMapping("remove/{id}")
    @Transactional
    public E6Wrapper<T>  removeById(@NotNull  @PathVariable ID id) {
        return baseSerivce.removeById(id);
    }

    /**
     * 批量移除（逻辑删除）
     *
     * @param i ID List
     * @return 批量移除成功的数量
     */
    @PostMapping("remove-batch")
    public E6Wrapper<T> removeBatch(@NotNull @RequestBody List<ID> i) {
        return baseSerivce.removeBatch(i);
    }

}
