/*
 * Copyright (c) 2016  athou(cai353974361@163.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.renovace;

import java.io.Serializable;

/**
 * The parse bean.
 * You can extends the bean in your app, and Renovace will parse you data into this bean<br>
 *
 * @author athou
 * @date 2016/10/26
 */
public class RenovaceBean<T> implements Serializable {
    private int code = 0;
    private String message;
    private String extra;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getExtra() {
        return extra;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return RenovaceCode.CODE_SUCCESS == this.code;
    }
}
