package cn.edu.lingnan.config;

import cn.edu.lingnan.common.JacksonObjectMapper;
import cn.edu.lingnan.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    //在拦截器中需要用到stringRedisTemplate，但拦截器没有交由spring容器管理，故无法通过@Autowired注入
    //但注册拦截器的配置类是交由spring容器管理的，所以可以通过构造方法获取stringRedisTemplate
    @Autowired  //注入stringRedisTemplate
    private StringRedisTemplate stringRedisTemplate;

    //设置静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源的映射!");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(stringRedisTemplate))  //构造方法传stringRedisTemplate
                .excludePathPatterns("/employee/login",
                        "/employee/logout",
                        "/backend/**",
                        "/front/**",
                        "/user/send",
                        "/user/login",
                        "/common/download",
                        "/common/upload"
                );
    }

    @Override  //拓展消息装换器,用于前后端信息互传时对消息进行装换(解决经度丢失，日期格式等问题)
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息装换器
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象装换器，底层使用jackson将java对象转为json
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息装换器对象追加到mvc框架的装换器中
        converters.add(0,mappingJackson2HttpMessageConverter);
    }
}
