package com.ry.sskt.core.annotation;

import com.ry.sskt.model.common.constant.UserTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 制定接口权限，允许用户类型
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowUserType {

    UserTypeEnum userType() default UserTypeEnum.All;

}
