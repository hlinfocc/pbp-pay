package net.hlinfo.pbp.pay.opt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
/**
 * 启用PBP工具注解，自动扫描net.hlinfo.pbp包，注入相关@Bean
 * @author cy
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ComponentScans(value= {@ComponentScan(value = {"net.hlinfo.pbp"})})
public @interface EnableHlinfoPBP {

}
