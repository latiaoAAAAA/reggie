package cn.edu.lingnan.utils;

//每次发送http请求时，都会创建一个新的线程，ThreadLocal在单前线程内共享
public class ThreadLocalUtil {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();
    public static void put(Long Id){
        THREAD_LOCAL.set(Id);
    }

    public static Long get(){
        return THREAD_LOCAL.get();
    }

    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
