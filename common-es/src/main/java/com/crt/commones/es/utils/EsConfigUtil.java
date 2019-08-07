package com.crt.commones.es.utils;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.crt.commones.es.annotation.EsField;
import com.crt.commones.es.annotation.EsIndex;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * ES 配置信息处理Util
 *
 * @author dreamingo
 */
public class EsConfigUtil {

    public static String buildEsConfig(Class clz) {
        if (null == clz || !clz.isAnnotationPresent(EsIndex.class)) {
            return null;
        }
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> settings = new HashMap<>();
        Map<String, Object> mappings = new HashMap<>();
        Map<String, Object> document = new HashMap<>();

        EsIndex esIndex = (EsIndex) clz.getAnnotation(EsIndex.class);
        settings.put("number_of_shards", esIndex.shards());
        settings.put("number_of_replicas", esIndex.replicas());
        settings.put("index.refresh_interval", esIndex.interval());
        config.put("settings", settings);

        Map<String, Object> properties = buildProperties(clz);

        document.put("properties", properties);

        String cName = esIndex.name();
        mappings.put(cName, document);
        config.put("mappings", mappings);

        return JSONUtil.toJsonStr(config);
    }

    public static Map<String, Object> buildProperties(Class clz) {
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> field;

        Field[] fieldList = clz.getFields();
        if (ArrayUtil.isNotEmpty(fieldList)) {
            for (Field fld : fieldList) {
                if (null == fld || !fld.isAnnotationPresent(EsField.class)) {
                    continue;
                }
                EsField esField = fld.getAnnotation(EsField.class);
                field = buildField(esField);
                if ("object".equals(esField.type())) {
                    Map<String, Object> innerProps = buildProperties(fld.getType());
                    field.put("properties", innerProps);
                }
                properties.put(fld.getName(), field);
            }
        }

        return properties;
    }

    public static Map<String, Object> buildField(EsField esField) {
        Map<String, Object> fields;
        //Map<String, Object> raw;
        Map<String, Object> keyword;
        Map<String, Object> field = new HashMap<>();
        field.put("type", esField.type());
        if (esField.fieldData()) {
            field.put("fielddata", true);
        }
        if (StrUtil.isNotBlank(esField.analyzer())) {
            field.put("analyzer", esField.analyzer());
            fields = new HashMap<>();
            //raw = new HashMap<>();
            keyword = new HashMap<>();
            /*if (StrUtil.isNotBlank(esField.rawType())) {
                raw.put("type", "keyword");
                field.put("raw", raw);
            }*/
            if (esField.innerFieldsKeyword()) {
                keyword.put("type", "keyword");
                fields.put("keyword", keyword);
                field.put("fields", fields);
            }
        }
        return field;
    }
}
