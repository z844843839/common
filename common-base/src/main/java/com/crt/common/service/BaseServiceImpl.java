package com.crt.common.service;

import com.crt.common.util.BeanUtil;
import com.crt.common.util.UserInfoUtil;
import com.crt.common.vo.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.lang.reflect.*;
import java.util.*;

/**
 * @Author : malin
 * @Description: 公共JPA实现
 * D 应用模块DAO继承JpaRepository 用于扩展自己的方法
 */
public class BaseServiceImpl<D extends JpaRepository<T, Integer>, T extends BaseEntity> implements BaseService<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    /**
     * Spring Data JPA 注入
     */
    @Autowired
    protected D dao;

    /**
     * 根据ID查询对应的实体是否存在
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
     * 新增前附加操作
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public E6Wrapper beforeSave(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 新增
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public E6Wrapper save(T entity) {
        //新增前，若有其他操作，在 beforeSave()方法中添加
        E6Wrapper before = beforeSave(entity);
        if(before.success()){
            entity = (T) before.getResult();
        }else{
            return before;
        }
        //设置创建人，创建时间，修改人，修改时间
        entity = setEntityOperationInfo(entity,"save");
        entity = dao.save(entity);
        //新增后，若有其他操作，在 afterSave()方法中添加
        E6Wrapper after = afterSave(entity);
        if (after.success()) {
            return E6WrapperUtil.ok();
        } else {
            return after;
        }
    }

    /**
     * 新增后附加操作
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public E6Wrapper afterSave(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 删除前附加操作
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public E6Wrapper beforeDelete(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 删除
     * @param id 主键ID
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
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
        }else{
            return before;
        }
        //执行删除
        dao.deleteById(entity.getId());
        return E6WrapperUtil.ok();
    }

    /**
     * 修改前的附加操作
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public E6Wrapper beforeModify(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 修改
     * @param entity 赋值给需要修改的字段，
     *               不需要修改的字段给null
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public E6Wrapper modify(Integer id, T entity) {
        if (null == entity) {
            return E6WrapperUtil.ok();
        }
        //修改前，若有其他操作，在 beforeModify()方法中添加
        E6Wrapper before = beforeModify(entity);
        if (before.success()) {
            entity = (T) before.getResult();
        } else {
            return before;
        }
        //根据主键ID查询实体
        Optional<T> optional = dao.findById(id);
        if (null == optional || !optional.isPresent()) {
            return E6WrapperUtil.paramError("实体不存在");
        }
            T obj = optional.get();
        //用传入实体值替换原有的实体值
//        Object oj = BeanUtil.cover(entity,obj);
        //设置修改人，修改时间
        Object oj = BeanUtil.Copy(obj,entity,false);
        T modifyObj = setEntityOperationInfo((T)oj,"modify");
        T newObj = dao.save(modifyObj);
        //修改后，若有其他操作，在 afterModify()方法中添加
        E6Wrapper after = afterModify(newObj);
        if (after.success()) {
            return E6WrapperUtil.ok();
        } else {
            return after;
        }
    }

    /**
     * 修改后的附加操作
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public E6Wrapper afterModify(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 根据主键ID查询实体
     * @param id 主键ID
     * @return
     */
    @Override
    public E6Wrapper<T> findById(Integer id) {
        Optional<T> optional = dao.findById(id);
        if (null == optional || !optional.isPresent()) {
            return E6WrapperUtil.paramError("实体不存在");
        }
        //查询后，若有其他操作，在 afterFind()方法中添加
        return afterFind(optional.get());
    }

    /**
     * 查询前附加操作
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public E6Wrapper beforeFind(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 查询实体
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
     * 查询后附加操作
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public E6Wrapper afterFind(T entity) {
        return E6WrapperUtil.ok(entity);
    }

    /**
     * 无参数查询实体集合
     * @return
     */
    @Override
    public E6Wrapper<List<T>> findListAll() {
        List<T> list = dao.findAll();
        return E6WrapperUtil.ok(list);
    }

    /**
     * 设置创建人创建时间修改人修改时间方法
     * @param entity
     * @param operationType
     * @return
     */
    protected T setEntityOperationInfo(T entity,String operationType){
        //获取当前登陆人
        E6Wrapper loginUser = UserInfoUtil.getUserInfo();
        UserRedisVO opUser = (UserRedisVO)loginUser.getResult();
        // 获取实体类的所有属性，返回Field数组
        Field[] field = FieldUtils.getAllFields(entity.getClass());
        String[] operationArray = new String[]{"createdId","createdBy","createdAt","modifiedId","modifiedBy","modifiedAt"};
        try {
            // 遍历所有属性
            for (int i = 0; i < field.length; i++) {
                // 获取属性的名字
                String name = field[i].getName();
                if (Arrays.asList(operationArray).contains(name)){
                    Date currntDate = new Date();
                    for (int j=0;j <operationArray.length;j++){
                        // 将属性的首字符大写，方便构造get，set方法
                        name = name.substring(0, 1).toUpperCase() + name.substring(1);
                        if ("save".equals(operationType)){
                            if ("CreatedId".equals(name)){
                                Method m = entity.getClass().getMethod("set"+name,Long.class);
//                                m.invoke(entity, opUser.getUserCode());
                                m.invoke(entity, opUser.getId().longValue());
                                break;
                            }
                            if ("CreatedBy".equals(name)){
                                Method m = entity.getClass().getMethod("set"+name,String.class);
                                m.invoke(entity, opUser.getRealName());
                                break;
                            }
                            if ("CreatedAt".equals(name)){
                                Method m = entity.getClass().getMethod("set"+name,Date.class);
                                m.invoke(entity, currntDate);
                                break;
                            }
                        }
                        if ("ModifiedId".equals(name)){
                            Method m = entity.getClass().getMethod("set"+name,Long.class);
//                            m.invoke(entity, opUser.getUserCode());
                            m.invoke(entity, opUser.getId().longValue());
                            break;
                        }
                        if ("ModifiedBy".equals(name)){
                            Method m = entity.getClass().getMethod("set"+name,String.class);
                            m.invoke(entity, opUser.getRealName());
                            break;
                        }
                        if ("ModifiedAt".equals(name)){
                            Method m = entity.getClass().getMethod("set"+name,Date.class);
                            m.invoke(entity,currntDate);
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            logger.error(" NoSuchMethodException ===> "+e.getMessage());
        } catch (SecurityException e) {
            logger.error(" SecurityException ===> "+e.getMessage());
        }catch (IllegalAccessException e) {
            logger.error(" IllegalAccessException ===> "+e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error(" InvocationTargetException ===> "+e.getMessage());
        }
        return entity;
    }
}
