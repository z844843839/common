package com.crt.common.service;

import com.crt.common.vo.BaseBizEntity;
import com.crt.common.vo.BaseEntity;
import com.crt.common.vo.E6Wrapper;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Author : malin
 * @Description: 公共JPA接口
 */
@NoRepositoryBean
public interface BaseService<T extends BaseEntity> {

    /**
     * 根据ID查询对应的实体是否存在
     * @param id
     * @return
     */
    E6Wrapper existsById(Integer id);

    /**
     * 新增前附加操作
     * @param entity
     * @return
     */
    E6Wrapper beforeSave(T entity);

    /**
     * 新增
     * @param entity
     * @return
     */
    E6Wrapper save(T entity);

    /**
     * 新增后附加操作
     * @param entity
     * @return
     */
    E6Wrapper afterSave(T entity);

    /**
     * 删除前附加操作
     * @param id
     * @return
     */
    E6Wrapper beforeDelete(Integer id);

    /**
     * 根据d删除
     * @param id 主键ID
     * @return
     */
    E6Wrapper deleteById(Integer id);

    /**
     * 修改前附加操作
     *
     * @param entity
     * @return
     */
    E6Wrapper beforeModify(T entity);

    /**
     * 修改
     *
     * @param id
     * @param entity
     * @return
     */
    E6Wrapper modify(Integer id, T entity);

    /**
     * 修改后附加操作
     *
     * @param entity
     * @return
     */
    E6Wrapper afterModify(T entity);

    /**
     * 根据主键ID查询实体
     *
     * @param id 主键ID
     * @return
     */
    E6Wrapper<T> findById(Integer id);

    /**
     * 查询前附加操作
     *
     * @param entity
     * @return
     */
    E6Wrapper beforeFind(T entity);

    /**
     * 根据主键ID查询实体
     *
     * @param entity
     * @return
     */
    E6Wrapper<T> findOne(T entity);

    /**
     * 无参数查询实体集合
     *
     * @return
     */
    E6Wrapper<List<T>> findListAll();

    /**
     * 查询后附加操作
     *
     * @param entity
     * @return
     */
    E6Wrapper afterFind(T entity);


}
