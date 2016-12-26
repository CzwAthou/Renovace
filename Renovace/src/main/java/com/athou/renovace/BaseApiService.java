/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
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

package com.athou.renovace;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by athou on 2016/10/26.
 */

public interface BaseApiService {

//    @GET("{url}")
//    Observable<ResponseBody> get(@Header("renovace_header") Map<String, String> header, @Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<ResponseBody> get(@Header("renovace_header") Map<String, String> header,
                                 @Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> maps);

//    @POST("{url}")
//    Observable<ResponseBody> post(@Path(value = "url", encoded = true) String url);

    @FormUrlEncoded
    @POST("{url}")
    Observable<ResponseBody> post(@Header("renovace_header") Map<String, String> header,
                                  @Path(value = "url", encoded = true) String url, @FieldMap Map<String, String> maps);

    @DELETE("{url}")
    Observable<ResponseBody> delete(@Header("renovace_header") Map<String, String> header,
                                    @Path("url") String url, @QueryMap Map<String, String> maps);

    @PUT("{url}")
    Observable<ResponseBody> put(@Header("renovace_header") Map<String, String> header,
                                 @Path("url") String url, @QueryMap Map<String, String> maps);

    @POST("{url}")
    Observable<ResponseBody> json(@Header("renovace_header") Map<String, String> header,
                                  @Path("url") String url, @Body RequestBody jsonStr);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> uploadFile(@Header("renovace_header") Map<String, String> header,
                                        @Path("url") String url,
                                        @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> uploadFiles(@Header("renovace_header") Map<String, String> header,
                                         @Path("url") String url,
                                         @Part("filename") String description,
                                         @PartMap() Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Header("renovace_header") Map<String, String> header,
                                          @Url String fileUrl);
}
