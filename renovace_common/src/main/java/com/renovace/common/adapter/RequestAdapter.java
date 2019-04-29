package com.renovace.common.adapter;

import com.renovace.common.request.RequestBuilder;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * @author athou
 * @date 2017/12/13
 */

public interface RequestAdapter {

    <T> Observable<T> adapt(@NonNull RequestBuilder builder);
}