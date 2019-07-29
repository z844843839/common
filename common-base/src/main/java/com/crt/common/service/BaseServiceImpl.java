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
public class BaseSerivceImpl<D extends JpaRepository<T, Integer>, T extends BaseEntity> implements BaseSerivce<T> {

    /**
     * Spring Data JPA 注入
     */
    @Autowired
    protected D dao;

    /**
     * 根据ID查询对应的实体是否存在
     *
     * @param id
     * @return
     */
    @Override
    public E6Wrapper existsById(Integer id) {
        Boolean exist = true;
        Optional<T> optional = dao.findById(id);
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
        //新增前，若有其他操作，在 beforeSave()方法中添加
        E6Wrapper before = beforeSave(entity);
        if(before.success()){
            entity = (T) before.getResult();
        }else{
            return before;
        }
        entity = dao.save(entity);
        //新增后，若有其他操作，在 afterSave()方法中添加
        E6Wrapper after = afterSave(entity);
        if (after.success()) {
            return E6WrapperUtil.ok(after.getResult());
        } else {
            return after;
        }
    }

    /**
     * 新增前附加操作
     *
     * @param entity
     * @return
     */
    @Transactional
    public E6Wrapper beforeSave(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 新增后附加操作
     *
     * @param entity
     * @return
     */
    @Transactional
    public E6Wrapper afterSave(T entity) {
        return E6WrapperUtil.ok(entity);
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
        //根据主键ID查询实体
        Optional<T> optional = dao.findById(id);
        if (null == optional || !optional.isPresent()) {
            return E6WrapperUtil.paramError("实体不存在");
        }
        T entity = optional.get();
        //删除前，若有其他操作，在 beforeDelete()方法中添加
        E6Wrapper before = beforeDelete(entity);
        if(before.success()){
            entity = (T) before.getResult();
            //执行删除
            dao.deleteById(entity.getId());
            return E6WrapperUtil.ok();
        }else{
            return before;
        }
    }

    /**
     * 删除前附加操作
     * @param entity
     * @return
     */
    @Transactional
    public E6Wrapper beforeDelete(T entity) {
        return E6WrapperUtil.ok(entity);
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
        if (null == entity) {
            return E6WrapperUtil.ok();
        }
        if (null == id || id == 0){
            id = entity.getId();
        }
        E6Wrapper e6Wrapper = beforeModify(entity);
        if (e6Wrapper.success()) {
            entity = (T) beforeModify(entity).getResult();
        } else {
            return e6Wrapper;
        }
        //根据主键ID查询实体
        Optional<T> optional = dao.findById(id);
        if (null == optional || !optional.isPresent()) {
            return E6WrapperUtil.paramError("实体不存在");
        }
        T obj = optional.get();
        //用传入实体值替换原有的实体值
        Object oj = BeanUtil.cover(obj, entity);
        T newObj = dao.save((T) oj);
        newObj = afterModify(newObj);
        return E6WrapperUtil.ok(newObj);
    }

    /**
     * 修改前的附加操作
     *
     * @param entity
     * @return
     */
    protected E6Wrapper beforeModify(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 修改后的附加操作
     *
     * @param entity
     * @return
     */
    @Transactional
    protected T afterModify(T entity) {
        return entity;
    }

    /**
     * 根据主键ID查询实体
     *
     * @param id 主键ID
     * @return
     */
    @Override
    public E6Wrapper<T> findById(Integer id) {
        Optional<T> optional = dao.findById(id);
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
        //查询前，若有其他操作，在 beforeFind()方法中添加
        E6Wrapper before = beforeFind(entity);
        if (before.success()){
            Example example = Example.of(entity);
            Optional<T> optional = dao.findOne(example);
            if (null == optional || !optional.isPresent()) {
                return E6WrapperUtil.paramError("实体不存在");
            }
            //查询后，若有其他操作，在 afterFind()方法中添加
            return afterFind(optional.get());
        }else{
            return before;
        }
    }

    /**
     * 查询前附加操作
     * @param entity
     * @return
     */
    @Transactional
    public E6Wrapper beforeFind(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 查询后附加操作
     * @param entity
     * @return
     */
    @Transactional
    public E6Wrapper afterFind(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 无参数查询实体集合
     *
     * @return
     */
    @Override
    public E6Wrapper<List<T>> findListAll() {
        List<T> list = dao.findAll();
        return E6WrapperUtil.ok(list);
    }

}
