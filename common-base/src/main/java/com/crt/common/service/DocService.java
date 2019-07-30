package com.crt.common.service;


import com.crt.common.vo.E6Wrapper;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public interface DocService<T> {

    /**
     * Excel导入
     * @param file
     * @param clazz
     * @param isExtend 是否继承父类字段 默认false
     * @return
     */
    E6Wrapper importExcel(MultipartFile file, Class<T> clazz, boolean isExtend);

    /**
     * 导出Excel
     * @param response
     * @param headAlias 标题别名
     * @param fileName  文件名称
     * @return
     */
    void exportExcel(HttpServletResponse response, LinkedHashMap<String,String> headAlias, String fileName);
}
