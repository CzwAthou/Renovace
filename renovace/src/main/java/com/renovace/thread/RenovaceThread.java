package com.renovace.thread;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * see {@link ThreadStrategy }
 *
 * @author athoucai
 * @date 2018/9/11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RenovaceThread {

    ThreadStrategy subscribeThread() default ThreadStrategy.IO;

    ThreadStrategy observeThread();
}
