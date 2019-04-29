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

/**
 * http request code. you can set value in your app
 * Created by athou on 2016/10/26.
 */
public class RenovaceCode {
    /**
     * 请求成功
     */
    public static int CODE_SUCCESS = 0x000;
    /**
     * 解析错误
     */
    public static int CODE_PARSE_ERR = 0x001;
    /**
     * 请求取消
     */
    public static int CODE_REQUEST_CANCEL = 0x002;
    /**
     * 下载失败
     */
    public static int CODE_DOWNLOAD_ERR = 0x003;

    public static int CODE_ERR = -1;
    public static int CODE_ERR_IO = -2;
    public static int CODE_ERR_UNKOWN = -3;
}
