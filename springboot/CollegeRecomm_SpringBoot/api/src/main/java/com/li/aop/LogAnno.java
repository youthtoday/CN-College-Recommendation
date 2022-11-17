package com.li.aop;

import java.lang.annotation.*;

/**
 * @Description: 日志注解
 * @Param:
 * @return:
 * @Author: XQD
 * @Date:2021/3/9 10:33
 */
@Target(ElementType.METHOD)  //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) // 注解在哪个阶段执行 运行时可见
public @interface LogAnno {
    // 记录日志的操作类型
    String operateType();
}
