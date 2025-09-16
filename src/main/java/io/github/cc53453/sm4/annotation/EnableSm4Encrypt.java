package io.github.cc53453.sm4.annotation;

import org.springframework.context.annotation.Import;

import io.github.cc53453.sm4.listener.AutoEncryptLocalFile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一般用在Main方法上，用于标注哪些配置文件需要加密。
 * 比如@EnableSm4Encrypt(filesPath = {"classpath:application.yml"})
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoEncryptLocalFile.class)
public @interface EnableSm4Encrypt {
    /**
     * 需要修改明文为密文的本地磁盘上的配置文件的路径。默认空列表。建议用classpath:路径的形式
     * @return 配置文件路径的list
     */
    String[] filesPath() default {};
}
