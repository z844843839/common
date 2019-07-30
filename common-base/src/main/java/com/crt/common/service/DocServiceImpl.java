package com.crt.common.service;

import com.crt.common.util.DocumentUtil;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import com.crt.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class DocServiceImpl<T,D extends JpaRepository<T, Integer>> implements DocService<T> {

    protected static final Logger logger = LoggerFactory.getLogger(DocServiceImpl.class);

    /**
     * Spring Data JPA 注入
     */
    @Autowired
    public D dao;

    /**
     * Excel导入
     *
     * @param file
     * @param clazz
     * @param isExtend 是否继承父类字段 默认false
     * @return
     */
    @Override
    public E6Wrapper importExcel(MultipartFile file, Class clazz, boolean isExtend) {
        List<T> result = null;
        String errorMsg = "";
        try{
            if (!file.isEmpty()) {
                if (DocumentUtil.checkFileType(file.getOriginalFilename())) {
                    result = DocumentUtil.importExcel(file, clazz, isExtend);
                }else{
                    errorMsg = "导入文件格式有误";
                }
            }else{
                errorMsg = "导入文件为空";
            }
        }catch (IOException e){
            return E6WrapperUtil.paramError(e.getMessage());
        }
        if (null != result){
            return E6WrapperUtil.ok(result);
        }else{
            return E6WrapperUtil.paramError(errorMsg);
        }
    }

    /**
     * 导出Excel
     *
     * @param response
     * @param headAlias 标题别名
     * @param fileName  文件名称
     * @return
     */
    @Override
    public void exportExcel(HttpServletResponse response, LinkedHashMap<String,String> headAlias, String fileName) {
        try {
            List<T> result = dao.findAll();
            DocumentUtil.exportExcel(response,headAlias,result,fileName);
        }catch (IllegalAccessException e){
            logger.error("export excel IllegalAccessException ======> " + e.getMessage());
        }catch (IOException e){
            logger.error("export excel IOException ======> " + e.getMessage());
        }
    }
}
