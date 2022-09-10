package cn.edu.lingnan.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;

public class OrderNumberWorder {
    public static String getNumber(){
        Snowflake snowflake = new Snowflake();
        return StrUtil.toString(snowflake.getGenerateDateTime(System.currentTimeMillis())+snowflake.nextId());
    }
}
