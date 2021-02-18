package com.ry.sskt.core.annotation;

import java.lang.annotation.*;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/18 15:50
 * @Description:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {

    String value() default "";

    int col() default 0;
}