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

package com.pince.renovace2;

import com.pince.renovace2.header.HeaderKey;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author athou
 * @date 2016/10/26
 */

public interface BaseApiService {

    /**
     * get 方法
     *
     * @param header
     * @param url
     * @param maps
     * @return
     */
    @GET("{url}")
    Observable<ResponseBody> get(@Header(HeaderKey.Header) Map<String, Object> header,
                                 @Path(value = "url", encoded = true) String url,
                                 @QueryMap Map<String, Object> maps);

    /**
     * post 方法
     *
     * @param header
     * @param url
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST("{url}")
    Observable<ResponseBody> post(@Header(HeaderKey.Header) Map<String, Object> header,
                                  @Path(value = "url", encoded = true) String url,
                                  @FieldMap Map<String, Object> maps);

    /**
     * delete 方法
     *
     * @param header
     * @param url
     * @param maps
     * @return
     */
    @DELETE("{url}")
    Observable<ResponseBody> delete(@Header(HeaderKey.Header) Map<String, Object> header,
                                    @Path("url") String url,
                                    @QueryMap Map<String, Object> maps);

    /**
     * put 方法
     *
     * @param header
     * @param url
     * @param maps
     * @return
     */
    @PUT("{url}")
    Observable<ResponseBody> put(@Header(HeaderKey.Header) Map<String, Object> header,
                                 @Path("url") String url,
                                 @QueryMap Map<String, Object> maps);

    /**
     * body 方法
     *
     * @param header
     * @param url
     * @param body
     * @return
     */
    @POST("{url}")
    Observable<ResponseBody> body(@Header(HeaderKey.Header) Map<String, Object> header,
                                  @Path(value = "url", encoded = true) String url,
                                  @Body RequestBody body);

}