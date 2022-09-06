package cn.edu.lingnan.utils;

import cn.edu.lingnan.entity.Employee;

public class ThreadLocalUtil {
    private static final ThreadLocal<Employee> THREAD_LOCAL = new ThreadLocal<Employee>();
    public static void put(Employee employee){
        THREAD_LOCAL.set(employee);
    }

    public static Employee get(){
        return THREAD_LOCAL.get();
    }

    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
