package com.highershine.portal.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HighershineLog {
    //操作类型
    String operateType() default "";
    //操作类型名称
    String operateTypeName() default "";
    //对应主数据ID(请使用arg0 第一个参数,arg1 第二个参数)
    String dataId() default "";
}
