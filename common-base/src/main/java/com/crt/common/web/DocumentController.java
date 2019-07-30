package com.crt.common.web;

import com.crt.common.service.DocService;
import com.crt.common.vo.E6Wrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;

/**
 * 文档导入导出的Controller的父类
 * S 业务Serivce 需要继承BaseSerivce
 * T 业务PO 继承BaseEntity 或 BaseBizEntity
 */
public class DocumentController<T,S extends DocService> {

    @Autowired
    protected S service;
    //导出文件名
    private String fileName = "新建文件";
    //导出Excel标题别名 Key实体属性，Value属性别名
    private LinkedHashMap<String,String> headAlias = null;

    @PostMapping("/importExcel")
    @ApiOperation(value = "Excel导入")
    @ResponseBody
    protected E6Wrapper importFile(MultipartFile file, boolean isExtend){
        //通过泛型T获取相应的class
        Class <T>  entityClass  =  (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return service.importExcel(file,entityClass,false);
    }

    @GetMapping("/exportExcel")
    @ApiOperation(value = "Excel导出")
    protected void exportExcel(HttpServletResponse response){
        service.exportExcel(response,getHeadAlias(),getFileName());
    }

    public String getFileName() {
        return fileName;
    }

    public LinkedHashMap<String, String> getHeadAlias() {
        return headAlias;
    }

}
