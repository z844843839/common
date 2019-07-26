package com.crt.common.service;

import com.crt.common.util.BeanUtil;
import com.crt.common.vo.BaseEntity;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author : malin
 * @Description: 公共JPA实现
 */
public class BaseSerivceImpl<T extends BaseEntity> implements BaseSerivce<T> {

    /**
     * Spring Data JPA 注入
     */
    @Autowired
    protected JpaRepository<T, Integer> repository;


    /**
     * 根据ID查询对应的实体是否存在
     *
     * @param id
     * @return
     */
    @Override
    public E6Wrapper existsById(Integer id) {
        Boolean exist = true;
        Optional<T> optional = repository.findById(id);
        if (null == optional || !optional.isPresent()) {
            exist = false;
        }
        return E6WrapperUtil.ok(exist);
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper save(T entity) {
        entity = repository.save(entity);
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 批量新增
     *
     * @param entities
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper batchSave(List<T> entities) {
        repository.saveAll(entities);
        return E6WrapperUtil.ok(entities);
    }


    /**
     * 删除
     *
     * @param id 主键ID
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper deleteById(Integer id) {
        repository.deleteById(id);
        return E6WrapperUtil.ok();
    }

    /**
     * 删除对象
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper delete(T entity) {
        repository.delete(entity);
        return E6WrapperUtil.ok();
    }

    /**
     * 批量删除对象
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper batchDelete(List<Integer> ids) {
        List<T> list = new ArrayList<>();
        for (Integer id : ids) {
            T entity = repository.getOne(id);
            list.add(entity);
        }
        repository.deleteInBatch(list);
        return E6WrapperUtil.ok();
    }


    /**
     * 修改
     *
     * @param entity 赋值给需要修改的字段，
     *               不需要修改的字段给null
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper modify(Integer id, T entity) {
        //根据主键ID查询实体
        Optional<T> optional = repository.findById(id);
        if (null == optional || !optional.isPresent()) {
            return E6WrapperUtil.paramError("实体不存在");
        }
        T obj = optional.get();
        //用传入实体值替换原有的实体值
        Object oj = BeanUtil.cover(obj, entity);
        return E6WrapperUtil.ok(repository.save((T) oj));
    }

    /**
     * 批量修改
     *
     * @param entities
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper batchModify(List<T> entities) {
        List<T> list = new ArrayList<>();
        for (T entity : entities) {
            //根据主键ID查询实体
            Optional<T> optional = repository.findById(entity.getId());
            if (null != optional && optional.isPresent()) {
                T obj = optional.get();
                //用传入实体值替换原有的实体值
                list.add((T) BeanUtil.cover(obj, entity));
            }
        }
        return E6WrapperUtil.ok(repository.saveAll(list));
    }

    /**
     * 根据主键ID查询实体
     *
     * @param id 主键ID
     * @return
     */
    @Override
    public E6Wrapper<T> findById(Integer id) {
        Optional<T> optional = repository.findById(id);
        if (null == optional || !optional.isPresent()) {
            return E6WrapperUtil.paramError("实体不存在");
        }
        return E6WrapperUtil.ok(optional.get());
    }

    /**
     * 查询实体
     *
     * @param entity
     * @return
     */
    @Override
    public E6Wrapper<T> findOne(T entity) {
        Example example = Example.of(entity);
        Optional<T> optional = repository.findOne(example);
        if (null == optional || !optional.isPresent()) {
            return E6WrapperUtil.paramError("实体不存在");
        }
        return E6WrapperUtil.ok(optional.get());
    }

    /**
     * 根据多主键ID查询实体集合
     *
     * @param ids
     * @return
     */
    @Override
    public E6Wrapper<List<T>> findAllById(List<Integer> ids) {
        List<Integer> list = new ArrayList<>();
        for (Integer id : ids) {
            if (id != null && id > 0) {
                list.add(id);
            }
        }
        return E6WrapperUtil.ok(repository.findAllById(list));
    }

    /**
     * 无参数查询实体集合
     *
     * @return
     */
    @Override
    public E6Wrapper<List<T>> findListAll() {
        List<T> list = repository.findAll();
        return E6WrapperUtil.ok(list);
    }


}
