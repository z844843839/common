package com.crt.common.web;

import com.crt.common.service.BaseService;
import com.crt.common.vo.BaseEntity;
import com.crt.common.vo.E6Wrapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 所有的Controller的父类
 * S 业务Serivce 需要继承BaseSerivce
 * T 业务PO 继承BaseEntity 或 BaseBizEntity
 * @author malin
 */
public abstract class BaseController<S extends BaseService, T extends BaseEntity>{

    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected S service;

    /**
     * 根据ID验证实体是否存在
     *
     * @param id 实体主键ID
     * @return
     */
    @PostMapping("/exists")
    @ApiOperation(value = "验证实体是否存在")
    protected E6Wrapper existsById(Integer id) {
        return service.existsById(id);
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增")
    protected E6Wrapper save(@RequestBody T entity) {
        return service.save(entity);
    }

    /**
     * 修改
     *
     * @param entity
     * @param id (可不填，将id值赋予entity)
     * @return
     */
    @PostMapping("/modify")
    @ApiOperation(value = "编辑")
    protected E6Wrapper modify(Integer id,@RequestBody T entity) {
        return service.modify(id, entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除")
    protected E6Wrapper delete(Integer id) {
        return service.deleteById(id);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据ID查询实体")
    protected E6Wrapper findById(Integer id) {
        return service.findById(id);
    }

    /**
     * 查询单个实体
     *
     * @param entity
     * @return
     */
    @PostMapping("/findOne")
    @ApiOperation(value = "查询实体")
    protected E6Wrapper findOne(@RequestBody T entity) {
        return service.findOne(entity);
    }

    /**
     * 查询实体集合
     *
     * @return
     */
    @PostMapping("/findListAll")
    @ApiOperation(value = "查询实体集合")
    protected E6Wrapper findList() {
        return service.findListAll();
    }

    /**
     * 把前台传来的时间戳转换为Date类型
     *
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyDateEditor());
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        CustomDateEditor dateEditor = new CustomDateEditor(df, true);
//        binder.registerCustomEditor(Date.class,dateEditor);
        //binder.registerCustomEditor(Date.class, new MyDateEditor());
    }

    private class MyDateEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            //通过两次异常的处理可以，绑定两次日期的格式
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(text);
            } catch (ParseException e) {
                format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = format.parse(text);
                } catch (ParseException e1) {
                    logger.error("系统异常:"+e1);
                }
            }
            setValue(date);
        }
    }
}
