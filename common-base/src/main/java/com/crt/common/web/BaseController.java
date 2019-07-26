package com.crt.common.web;

import com.crt.common.service.AbstractUserService;
import com.crt.common.service.BaseSerivce;
import com.crt.common.vo.AbstractUser;
import com.crt.common.vo.BaseEntity;
import com.crt.common.vo.E6Wrapper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 所有的Controller的父类
 * S 业务Serivce 需要继承BaseSerivce
 * T 业务PO 继承BaseEntity 或 BaseBizEntity
 */
public abstract class BaseController<S extends BaseSerivce, T extends BaseEntity> {

    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired(required = false)
    private AbstractUserService abstractUserService;

    @Autowired
    protected S serivce;

    /**
     * 登陆用户信息
     *
     * @return
     */
    public AbstractUser getLoginUser() {
        return abstractUserService.getLoginUser();
    }

    /**
     * 根据ID验证实体是否存在
     *
     * @param id 实体主键ID
     * @return
     */
    @PostMapping("/exists")
    @ApiOperation(value = "验证实体是否存在")
    protected E6Wrapper existsById(Integer id) {
        return serivce.existsById(id);
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增")
    protected E6Wrapper save(T entity) {
        return serivce.save(entity);
    }

    /**
     * 修改
     *
     * @param entity
     * @return
     */
    @PostMapping("/modify")
    @ApiOperation(value = "编辑")
    protected E6Wrapper modify(Integer id, T entity) {
        return serivce.modify(id, entity);
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
        return serivce.deleteById(id);
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
        return serivce.findById(id);
    }

    /**
     * 查询单个实体
     *
     * @param entity
     * @return
     */
    @PostMapping("/findOne")
    @ApiOperation(value = "查询实体")
    protected E6Wrapper findOne(T entity) {
        return serivce.findOne(entity);
    }

    /**
     * 查询实体集合
     *
     * @return
     */
    @PostMapping("/findListAll")
    @ApiOperation(value = "查询实体集合")
    protected E6Wrapper findList() {
        return serivce.findListAll();
    }

    /**
     * 把前台传来的时间戳转换为Date类型
     *
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyDateEditor());
    }

    private class MyDateEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) {
            try {
                if (StringUtils.isBlank(text)) {
                    setValue(null);
                    return;
                }
                long timeStamp = new Long(text);
                Date date = new Date(timeStamp);
                setValue(date);
            } catch (Exception e) {
                logger.error("从前台传来的时间戳错误，请检查前台传来的时间戳");
            }
        }
    }
}
