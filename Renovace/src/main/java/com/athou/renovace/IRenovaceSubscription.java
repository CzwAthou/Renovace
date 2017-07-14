package com.athou.renovace;

import java.lang.reflect.Type;

/**
 * Created by athou on 2017/5/12.
 */
public interface IRenovaceSubscription {
    /**
     * get parse javabean type
     * @return
     */
    Type getType();

    /**
     * if Subscriber is unsubscribed
     * @return
     */
    boolean isUnsubscribed();
}