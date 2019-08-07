package com.crt.commones.es.service;

import com.crt.common.vo.BaseEntity;
import com.crt.common.vo.E6Wrapper;
import com.crt.commones.es.pojo.ModelEs;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author : masibin
 * @Description: 公共JPA接口
 */
@NoRepositoryBean
public interface EsBaseSerivce<T extends ModelEs,ID extends Serializable> {

    /**
     * 查询实体集合
     * 可以自定义sql
     *
     * @return
     */
    E6Wrapper<List<T>> listSql(String sql, Map map);


    /**
     * 无参数查询实体集合
     *
     * @return
     */
    E6Wrapper<List<T>> findListAll();

    /**
     * 统计总数查询全部条数
     * @return
     */
    E6Wrapper<Long> count();


    /**
     * 统计总数 可以自定sql
     * @param sql
     * @return
     */
    E6Wrapper<Long> countSql(String sql, Map map);


    /**
     * 查看：根据ID查找
     * <p>
     * @param id 主键
     * @return 查找到的对象
     */
    E6Wrapper<T> show(ID id);


    /**
     * 创建
     *
     * @param obj 需创建的对象
     * @return 创建后的对象
     */
    E6Wrapper<T> create(T obj);

    /**
     * 更新
     *
     * @param id  主键
     * @param obj 新对象
     * @return 更新后的对象
     */
    E6Wrapper<T> modify(ID id, T obj);


    /**
     * 删除（物理删除）：根据ID
     * 方法一：直接使用dao根据ID删除
     * dao.deleteById(id);
     *
     * @param id 主键
     */
    E6Wrapper<T> deleteById(ID id);

    /**
     * 移除（逻辑删除）：根据ID
     *
     * @param id 主键
     */
    E6Wrapper<T> removeById(ID id);


    /**
     * 批量移除（逻辑删除）
     *
     * @param i ID List
     * @return 批量移除成功的数量
     */
    E6Wrapper<T>  removeBatch(List<ID> i);
}
