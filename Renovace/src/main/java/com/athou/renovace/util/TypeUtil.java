package com.athou.renovace.util;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by athou on 2017/5/12.
 */

public class TypeUtil {

    public static Type newParameterizedTypeWithOwner(Type rawType, Type typeArguments) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments);
    }

    /**
     * find the type by interfaces
     *
     * @param cls
     * @param <R>
     * @return
     */
    public static <R> Type findNeedType(Class<R> cls) {
        List<Type> typeList = getMethodTypes(cls);
        if (typeList == null || typeList.isEmpty()) {
            return RequestBody.class;
        }
        return typeList.get(0);
    }

    /**
     * MethodHandler
     */
    public static <T> List<Type> getMethodTypes(Class<T> cls) {
        Type typeOri = cls.getGenericSuperclass();
        List<Type> needtypes = null;
        // if Type is T
        if (typeOri instanceof ParameterizedType) {
            needtypes = new ArrayList<>();
            Type[] parentypes = ((ParameterizedType) typeOri).getActualTypeArguments();
            for (Type childtype : parentypes) {
                needtypes.add(childtype);
                if (childtype instanceof ParameterizedType) {
                    Type[] childtypes = ((ParameterizedType) childtype).getActualTypeArguments();
                    for (Type type : childtypes) {
                        needtypes.add(type);
                    }
                }
            }
        }
        return needtypes;
    }
}