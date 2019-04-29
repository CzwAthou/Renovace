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

package com.renovace.Util;

import android.util.Log;

/**
 * @author athou
 * @date 2016/10/26
 */
public final class RenovaceLogUtil {
    public static boolean DEBUG = true;
    private static String TAG = "Renovace";

    public static void logI(String msg) {
        if (DEBUG && msg != null) {
            Log.i(TAG, msg);
        }
    }

    public static void logD(String msg) {
        if (DEBUG && msg != null) {
            Log.d(TAG, msg);
        }
    }

    public static void logE(String msg) {
        if (DEBUG && msg != null) {
            Log.e(TAG, msg);
        }
    }
}