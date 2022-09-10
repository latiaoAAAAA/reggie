package cn.edu.lingnan.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RedisUtils {

    //实体类转map，以hash存入redis
    public static <T> Map<String,Object> BeanToMap(T t,String... ignoreProperties){
        Map<String, Object> map = BeanUtil.beanToMap(t, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreProperties(ignoreProperties)
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString())
        );
        return map;
    }


    //按时间顺序获取所有pojo
    public static <T, U extends Comparable<? super U>> List<T> getList(
            StringRedisTemplate stringRedisTemplate,  //redis的java客户端
            String redisKey,  //获取所有key，例employee:data:*
            Class<T> pojoClass,  //用于实例化对象
            Function<? super T, ? extends U> sortField,  //排序字段
            int page,
            int pageSize
    ) {
        Set<String> keys = stringRedisTemplate.keys(redisKey);
        List<T> tList = keys.stream().map(key -> {
            T resT = null;
            try {
                //由于泛型不能实例化，故需通过Class对象
                resT = BeanUtil.fillBeanWithMap(stringRedisTemplate.opsForHash().entries(key),pojoClass.newInstance(),true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return resT;
        }).sorted(Comparator.comparing(sortField)).collect(Collectors.toList());
        Collections.reverse(tList);
        int last=page * pageSize;
        if (last> keys.size()){
            last= keys.size();
        }
        List<T> lastList = tList.subList((page - 1) * pageSize, last);
        return lastList;
    }


    //获取单个pojo
    public static <T, U extends Comparable<? super U>> T getOne(
            StringRedisTemplate stringRedisTemplate,  //redis的java客户端
            String redisKey,  //key
            Class<T> pojoClass  //用于实例化对象
    ) {
        T t = null;
        try {
            t = BeanUtil.fillBeanWithMap(stringRedisTemplate.opsForHash().entries(redisKey), pojoClass.newInstance(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }
}
