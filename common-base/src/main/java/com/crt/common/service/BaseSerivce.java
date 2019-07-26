package com.crt.common.service;

import com.crt.common.vo.BaseEntity;
import com.crt.common.vo.E6Wrapper;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Author : malin
 * @Description: 公共JPA接口
 */
@NoRepositoryBean
public interface BaseSerivce<T extends BaseEntity> {

    /**
     * 根据ID查询对应的实体是否存在
     *
     * @param id
     * @return
     */
    E6Wrapper existsById(Integer id);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    E6Wrapper save(T entity);

    /**
     * @param entities
     * @return
     */
    E6Wrapper batchSave(List<T> entities);

    /**
     * 根据d删除
     *
     * @param id 主键ID
     * @return
     */
    E6Wrapper deleteById(Integer id);

    /**
     * 删除
     *
     * @param entity
     * @return
     */
    E6Wrapper delete(T entity);

    /**
     * 根据ID批量删除
     *
     * @param ids
     * @return
     */
    E6Wrapper batchDelete(List<Integer> ids);

    /**
     * 修改
     *
     * @param id
     * @param entity
     * @return
     */
    E6Wrapper modify(Integer id, T entity);

    /**
     * 批量修改
     *
     * @param entities
     * @return
     */
    E6Wrapper batchModify(List<T> entities);

    /**
     * 根据主键ID查询实体
     *
     * @param id 主键ID
     * @return
     */
    E6Wrapper<T> findById(Integer id);

    /**
     * 根据主键ID查询实体
     *
     * @param entity
     * @return
     */
    E6Wrapper<T> findOne(T entity);


    /**
     * 根据多主键ID查询实体集合
     *
     * @param ids
     * @return
     */
    E6Wrapper<List<T>> findAllById(List<Integer> ids);

    /**
     * 无参数查询实体集合
     *
     * @return
     */
    E6Wrapper<List<T>> findListAll();


}
