package com.crt.common.util;


import com.crt.common.vo.PageParamVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * 分页类
 */
public class PageUtil {

    /**
     * Jpa需要的分页对象
     *
     * @param page
     * @return
     */
    public static PageRequest buildPageable(PageParamVO page) {
        Sort sort = Sort.unsorted();
        if (StringUtils.isEmpty(page.getSortIndx())) {
            page.setSortIndx("modifiedTime");
        }
        if (StringUtils.isEmpty(page.getSortDir())) {
            page.setSortDir(Sort.Direction.DESC.name());
        }
        sort = new Sort(Sort.Direction.fromString(page.getSortDir()), page.getSortIndx());
        if (page.getCurPage() == null||page.getCurPage() <= 0) {
            page.setCurPage(1);
        }
        if (page.getPageSize() == null) {
            page.setPageSize(10000);
        }
        PageRequest pageRequest = PageRequest.of(page.getCurPage() - 1, page.getPageSize(), sort);
        return pageRequest;
    }

    /**
     * 填充数据
     *
     * @param pageParam
     * @param page
     * @return
     */
    public static PageParamVO fillPageParamResult(PageParamVO pageParam, Page page) {
        pageParam.setData(page.getContent());
        pageParam.setTotalRecords(page.getTotalElements());
        return pageParam;
    }
}
