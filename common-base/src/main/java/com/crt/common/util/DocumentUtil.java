package com.crt.common.util;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

public class DocumentUtil {

    /**
     * 根据文件名后缀，检查文件  xls、xlsx返回true 其他返回false
     *
     * @param fileName
     * @return
     */
    public static boolean checkFileType(String fileName) {
        boolean sign = false;
        if (fileName.matches("^.+\\.(?i)(xls)$")) {
            sign = true;
        }
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            sign = true;
        }
        return sign;
    }

    /**
     * 导入Excel
     * <p>
     * 描述：
     * 适用同实体单个或多个Sheet页Excel文件导入
     * 格式：
     * .xls .xlsx
     * </p>
     *
     * @param file     导入的文件
     * @param clazz    映射的实体类
     * @param isExtend 实体是否继承父类字段
     * @return List<T>
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz, boolean isExtend) throws IOException {
        List<T> result = null;
        //默认获取第一页
        ExcelReader readerMode = ExcelUtil.getReader(file.getInputStream());
        //获取表头
        //startRowIndex 起始行（包含，从0开始计数）
        List<Object> heads = readerMode.readRow(0);
        Field[] field = null;
        /**
         * isExtend 为true 读取实体及其父类的属性
         *         为fasle 只读取当前实体的属性
         */
        if (isExtend) {
            //获取实体及其父类的属性
            field = FieldUtils.getAllFields(clazz);
        } else {
            // 获取实体类的所有属性
            field = clazz.getDeclaredFields();
        }
        if (heads.size() >= field.length) {
            for (int i = 0; i < heads.size(); i++) {
                readerMode.addHeaderAlias(heads.get(i).toString(), field[i].getName());
            }
        }
        result = readerMode.readAll(clazz);
        readerMode.close();
        return result;
    }

//    public static <T> void export(HttpServletResponse response, LinkedHashMap<String, String> headAlias, List<T> result, String fileName)throws IOException,UnsupportedEncodingException {
//       ExcelWriter writer = ExcelUtil.getWriter(true);
//       writer.write(result);
//       response.reset();
//       response.setContentType("application/octet-stream;charset=UTF-8");
//       response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx","utf-8"));
//       writer.flush(response.getOutputStream());
//    }

    /**
     * 导出Excel
     *
     * @param response  必填
     * @param headAlias 非必填，可为null
     * @param result    非必填，可为null
     *                  headAlias 为null 则 result 也为null 导出空Excel *
     * @param fileName  必填
     * @param <T>
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void exportExcel(HttpServletResponse response, LinkedHashMap<String, String> headAlias, List<T> result, String fileName) throws IllegalAccessException, IOException {
        // 创建excel工作簿 SXSSFWorkbook 是专门用于大数据了的导出　　
        Workbook wb = new XSSFWorkbook();
//        // 创建两种单元格格式
//        CellStyle cs = wb.createCellStyle();
//        CellStyle cs2 = wb.createCellStyle();
//        // 创建两种字体
//        Font f = wb.createFont();
//        Font f2 = wb.createFont();
//        // 创建第一种字体样式（用于列名）
//        f.setFontHeightInPoints((short) 10);
//        f.setColor(IndexedColors.BLACK.getIndex());
//        // 创建第二种字体样式（用于值）
//        f2.setFontHeightInPoints((short) 10);
//        f2.setColor(IndexedColors.BLACK.getIndex());
//        // 设置第一种单元格的样式（用于列名）
//        cs.setFont(f);
//        // 设置第二种单元格的样式（用于值）
//        cs2.setFont(f2);
        // 创建sheet
        Sheet sheet = wb.createSheet();
        //设置Excel标题
        //标题别名设置为null，则按照实体属性全量导出
        //标题别名为null result一定不能为null 否则导出空Excel
        if (null != result && result.size() > 0) {
            T entity = result.get(0);
            Class c = entity.getClass();
            Field[] fs = c.getDeclaredFields();
            if (null == headAlias || headAlias.size() == 0) {
                headAlias = new LinkedHashMap<>();
                for (int i = 0; i < fs.length; i++) {
                    //类中的成员变量为private,故必须进行此操作
                    fs[i].setAccessible(true);
                    headAlias.put(fs[i].getName(), "");
                }
            }
        }
        // 全表头处理
        if (null != headAlias && headAlias.size() > 0) {
            // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
            for (int i = 0; i < headAlias.size(); i++) {
                sheet.setColumnWidth(i, (short) (35.7 * 300));
            }
            // 创建第一行
            Row row = sheet.createRow(0);
            int i = 0;
            //设置标题内容，有别名显示别名
            for (Map.Entry<String, String> entry : headAlias.entrySet()) {
                if (i < headAlias.size()) {
                    Cell cell = row.createCell(i);
                    if (StringUtils.isEmpty(entry.getValue())) {
                        cell.setCellValue(entry.getKey());
                    } else {
                        cell.setCellValue(entry.getValue());
                    }
                }
                i++;
            }
        }
        // 设置Excel内容
        if (null != result && result.size() > 0) {
            //将List<T> 转换为 List<Map<String,Object>>
            List<Map<String, Object>> list = beanToMapList(result);
            for (int i = 0; i < list.size(); i++) {
                // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
                // 创建一行，在页sheet上
                // 内容行从1开始 0行为表头
                Row row1 = sheet.createRow(i + 1);
                // 在row行上创建一个方格
                int j = 0;
                for (Map.Entry<String, String> entry : headAlias.entrySet()) {
                    Cell cell = row1.createCell(j);
                    if ("createdAt".equals(entry.getKey()) || "modifiedAt".equals(entry.getKey())){
                        String dateStr = "";
                        if (null != list.get(i).get(entry.getKey())){
                            dateStr =  DateUtils.dateStr((Date) list.get(i).get(entry.getKey()));
                        }
                        cell.setCellValue(dateStr);
                    }else {
                        cell.setCellValue(list.get(i).get(entry.getKey()) == null ? " " : list.get(i).get(entry.getKey()).toString());
                    }
                    j++;
                }
            }
        }
        // ----------- Excel创建完成 ---------------
        // 开始设定下载参数
//        fileName = new String((fileName+".xlsx").getBytes("gb2312"), "iso8859-1");
        fileName = new String(java.net.URLEncoder.encode(fileName+".xlsx", "UTF-8"));
        // 如果文件名是英文名不需要加编码格式，如果是中文名需要添加"iso-8859-1"防止乱码
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        // 开始处理下载的流
        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
        wb.close();
        if (out != null) {
            out.close();
        }
    }

    /**
     * 把字符串转成utf8编码，保证中文文件名不会乱码
     *
     * @param s
     * @return
     */
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0){
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * List<T> 转为 List<Map<String,Object>>
     *
     * @param list
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private static <T> List<Map<String, Object>> beanToMapList(List<T> list) throws IllegalAccessException {
        List<Map<String, Object>> result = new ArrayList<>();
        for (T entity : list) {
            Class c = entity.getClass();
            Field[] fs = c.getDeclaredFields();
            Map<String, Object> field = new LinkedHashMap<>();
            for (Field f : fs) {
                f.setAccessible(true);
                field.put(f.getName(), f.get(entity));
            }
            result.add(field);
        }
        return result;
    }

    public void exportWord(HttpServletResponse response, String content) throws IOException {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph para;
        XWPFRun run;
        para = doc.createParagraph();
        para.setAlignment(ParagraphAlignment.LEFT);//设置左对齐
        run = para.createRun();
        run.setFontFamily("微软雅黑");
        run.setFontSize(13);
        run.setText(content);
        doc.createParagraph();
        OutputStream os = response.getOutputStream();
        doc.write(os);
    }
}
