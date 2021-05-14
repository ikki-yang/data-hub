package com.issac.studio.app.util;

import com.alibaba.fastjson.JSONObject;
import com.issac.studio.app.exception.LengthException;
import com.issac.studio.app.exception.TypeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 * @description: 类型转换工具类
 * @file: TypeUtil
 * @author: issac.young
 * @date: 2020/12/1 11:03 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class TypeUtil {
    public static DataType typeMap(String typeName) {
        DataType dataType;
        switch (typeName) {
            case "string":
                dataType = DataTypes.StringType;
                break;
            case "integer":
                dataType = DataTypes.IntegerType;
                break;
            case "double":
                dataType = DataTypes.DoubleType;
                break;
            case "float":
                dataType = DataTypes.FloatType;
                break;
            case "long":
                dataType = DataTypes.LongType;
                break;
            default:
                String msg = String.format("类型{%s}不受支持！", typeName);
                throw new TypeException(msg);
        }
        return dataType;
    }

    public static Object[] typeFormat(String[] source, StructType schema) {
        StructField[] fields = schema.fields();
        Object[] target = new Object[fields.length];

        if (fields.length != source.length) {
            String[] fieldNames = new String[fields.length];
            String[] fieldTypeNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fieldNames[i] = fields[i].name();
                fieldTypeNames[i] = fields[i].dataType().typeName();
            }
            String msg = String.format("source中的field的数量与schema中的field数量不一致！source={%d}, schema={%d}", source.length, fields.length);
            msg += JSONObject.toJSONString(source);
            msg += "-------------fields=";
            msg += JSONObject.toJSONString(fieldNames);
            msg += "-------------fieldType=";
            msg += JSONObject.toJSONString(fieldTypeNames);
            throw new LengthException(msg);
        }

        for (int i = 0; i < fields.length; i++) {
            StructField field = fields[i];
            DataType dataType = field.dataType();
            try {
                switch (dataType.typeName()) {
                    case "string":
                        // 当为string时，不用转换
                        target[i] = source[i].trim();
                        break;
                    case "integer":
                        if (StringUtils.isNotBlank(source[i]) && StringUtils.isNumeric(source[i].trim())) {
                            target[i] = Integer.valueOf(source[i].trim());
                        } else {
                            target[i] = 0;
                        }
                        break;
                    case "double":
                        if (StringUtils.isNotBlank(source[i])) {
                            String replaced = source[i].trim().replaceFirst("\\.", "");
                            if (StringUtils.isNumeric(replaced)) {
                                target[i] = Double.valueOf(source[i].trim());
                            } else {
                                target[i] = 0.00D;
                            }
                        } else {
                            target[i] = 0.00D;
                        }
                        break;
                    case "float":
                        if (StringUtils.isNotBlank(source[i])) {
                            String replaced = source[i].trim().replaceFirst("\\.", "");
                            if (StringUtils.isNumeric(replaced)) {
                                target[i] = Float.valueOf(source[i].trim());
                            } else {
                                target[i] = 0.00F;
                            }
                        } else {
                            target[i] = 0.00F;
                        }
                        break;
                    case "long":
                        if (StringUtils.isNotBlank(source[i]) && StringUtils.isNumeric(source[i].trim())) {
                            target[i] = Long.valueOf(source[i].trim());
                        } else {
                            target[i] = 0L;
                        }
                        break;
                    default:
                        String msg = String.format("类型{%s}不受支持！", dataType.typeName());
                        throw new TypeException(msg);
                }
            } catch (Exception e) {
                String[] fieldNames = new String[fields.length];
                String[] fieldTypeNames = new String[fields.length];
                for (int k = 0; k < fields.length; k++) {
                    fieldNames[k] = fields[k].name();
                    fieldTypeNames[k] = fields[k].dataType().typeName();
                }
                String msg = String.format("数据类型转换过程出错！fieldName=%s, fieldTypeName=%s", fieldNames[i], fieldTypeNames[i]);
                msg += "\n";
                msg += e;
                throw new TypeException(msg);
            }
        }
        return target;
    }
}
