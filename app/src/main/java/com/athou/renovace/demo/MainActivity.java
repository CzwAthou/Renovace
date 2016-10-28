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

package com.athou.renovace.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.athou.renovace.Renovace;
import com.athou.renovace.RenovaceCallback;
import com.athou.renovace.bean.RenovaceBean;
import com.athou.renovace.demo.bean.AreaItemBean;
import com.athou.renovace.demo.bean.GuideAlbumListBean;
import com.athou.renovace.demo.bean.SouguBean;
import com.athou.renovace.demo.bean.StoryListBean;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by athou on 2016/10/10.
 */

public class MainActivity extends AppCompatActivity {
    String baseUrl = "http://ip.taobao.com/";

    private String TAG = "Renovace";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MainActivity");
        Renovace.getInstance().init("http://base.wanzi.cc");
    }

    public void OnClick1(View v) {
        Renovace.getInstance().get("/area/getAreaAll", new RenovaceCallback<RenovaceBean<List<AreaItemBean>>>(this) {
            @Override
            public <T> void onSuccees(T response) {
                Log.i(TAG, "onSuccees");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void OnClick2(View v) {
        Renovace.getInstance().get("/area/getAreaAll", new Subscriber<RenovaceBean<List<AreaItemBean>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(RenovaceBean<List<AreaItemBean>> listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });
    }

    public void OnClick3(View v) {
        Renovace.getInstance().get("/area/getAreaAll", new RenovaceCallback<ResponseBody>(this) {
            @Override
            public <T> void onSuccees(T response) {
                Log.i(TAG, "onSuccees");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void OnClick4(View v) {
        Renovace.getInstance().get("/area/getAreaAll", new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(ResponseBody listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });
    }

    public void OnClick5(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", "5697074ce97ca");
        Renovace.getInstance().get("/album/getAlbumList", parameters, new RenovaceCallback<RenovaceBean<GuideAlbumListBean>>(this) {
            @Override
            public <T> void onSuccees(T response) {
                Log.i(TAG, "onSuccees");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void OnClick6(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", "5697074ce97ca");
        Renovace.getInstance().get("/album/getAlbumList", parameters, new Subscriber<RenovaceBean<GuideAlbumListBean>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(RenovaceBean<GuideAlbumListBean> listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });
    }

    public void OnClick7(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", "5697074ce97ca");
        Renovace.getInstance().get("/album/getAlbumList", parameters, new RenovaceCallback<ResponseBody>(this) {
            @Override
            public <T> void onSuccees(T response) {
                Log.i(TAG, "onSuccees");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void OnClick8(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", "5697074ce97ca");
        Renovace.getInstance().get("/album/getAlbumList", parameters, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(ResponseBody listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });
    }

    // Post =========================================================================================

    public void OnClick9(View v) {
        Renovace.getInstance().post("/area/getAreaAll", new RenovaceCallback<RenovaceBean<List<AreaItemBean>>>(this) {
            @Override
            public <T> void onSuccees(T response) {
                Log.i(TAG, "onSuccees");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void OnClick10(View v) {
        Renovace.getInstance().post("/area/getAreaAll", new Subscriber<RenovaceBean<List<AreaItemBean>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(RenovaceBean<List<AreaItemBean>> listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });
    }

    public void OnClick11(View v) {
        Renovace.getInstance().post("/area/getAreaAll", new RenovaceCallback<ResponseBody>(this) {
            @Override
            public <T> void onSuccees(T response) {
                Log.i(TAG, "onSuccees");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void OnClick12(View v) {
        Renovace.getInstance().post("/area/getAreaAll", new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(ResponseBody listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });
    }

    public void OnClick13(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("offset", 0 + "");
        parameters.put("limit", 20 + "");
        parameters.put("area", "52c90c30512bc");
        parameters.put("keyword", "");
        Renovace.getInstance().post("/story/getStoryList", parameters, new RenovaceCallback<RenovaceBean<StoryListBean>>(this) {
            @Override
            public <T> void onSuccees(T response) {
                Log.i(TAG, "onSuccees");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void OnClick14(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("offset", 0 + "");
        parameters.put("limit", 20 + "");
        parameters.put("area", "52c90c30512bc");
        parameters.put("keyword", "");
        Renovace.getInstance().post("/story/getStoryList", parameters, new Subscriber<RenovaceBean<StoryListBean>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(RenovaceBean<StoryListBean> listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });
    }

    public void OnClick15(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("offset", 0 + "");
        parameters.put("limit", 20 + "");
        parameters.put("area", "52c90c30512bc");
        parameters.put("keyword", "");
        Renovace.getInstance().post("/story/getStoryList", parameters, new RenovaceCallback<ResponseBody>(this) {
            @Override
            public <T> void onSuccees(T response) {
                Log.i(TAG, "onSuccees");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void OnClick16(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("offset", 0 + "");
        parameters.put("limit", 20 + "");
        parameters.put("area", "52c90c30512bc");
        parameters.put("keyword", "");
        Renovace.getInstance().post("/story/getStoryList", parameters, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(ResponseBody listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });
    }

    public void OnClick17(View v) {
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("apikey", "27b6fb21f2b42e9d70cd722b2ed038a9");
//        Renovace.getInstance().init("https://apis.baidu.com/", new IRenovace.IHttpClient() {
//            @Override
//            public OkHttpClient getHttpClient() {
//                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
//                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
//                return new OkHttpClient.Builder()
//                        .addInterceptor(logInterceptor)
//                        .addInterceptor(new HeaderInterceptor(headers))
//                        .retryOnConnectionFailure(true)
//                        .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
//                        .build();//设置超时;
//            }
//        });
//        Renovace.getInstance().get("https://apis.baidu.com/apistore/weatherservice/cityname?cityname=上海", new Subscriber<ResponseBody>() {
//            @Override
//            public void onCompleted() {
//                Log.i(TAG, "onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i(TAG, "onError");
//            }
//
//            @Override
//            public void onNext(ResponseBody listRenovaceBean) {
//                Log.i(TAG, "onSuccees");
//            }
//        });

        Renovace.getInstance().init("http://gank.io");
        Renovace.getInstance().get("/api/data/Android/10/1", new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(ResponseBody listRenovaceBean) {
                Log.i(TAG, "onSuccees");
            }
        });

//        Renovace.getInstance().init("http://ip.taobao.com/");
//        Map<String, String> parameters = new HashMap<String, String>();
//        parameters.put("ip", "21.22.11.33");
//        Renovace.getInstance().post("service/getIpInfo.php", parameters, new RenovaceCallback<DataBean<ResultModel>>(this) {
//            @Override
//            public <ResultModel> void onSuccees(ResultModel response) {
//                Log.i(TAG, "onSuccees");
//            }
//        });
    }

    public void OnClick18(View v) {
        Renovace.getInstance().init("http://lbs.sougu.net.cn/");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("m", "souguapp");
        parameters.put("c", "appusers");
        parameters.put("a", "network");

        TestApi testApi = Renovace.getInstance().create(TestApi.class);
//        Renovace.getInstance().call(testApi.getSougu(parameters), new Subscriber<SouguBean>() {
//
//            @Override
//            public void onCompleted() {
//                Log.i(TAG, "onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i(TAG, "onError");
//            }
//
//            @Override
//            public void onNext(SouguBean souguBean) {
//                Log.i(TAG, "onSuccees");
//            }
//        });
        Renovace.getInstance().call(testApi.getSougu(parameters), new RenovaceCallback<SouguBean>(this) {
            @Override
            public <SouguBean> void onSuccees(SouguBean response) {
                Log.i(TAG, "onSuccees");
            }
        });
    }
}
