# Renovace 
###一款基于Retrofit & RxJava 实现简单易用的网络请求框架

Retrofit和Rxjava也许是当下异常火爆的2个开源框架，均来自神一般的Square公司。网上现在也已经有了许多对这2个框架的介绍了，
参考大神们的文章
 [RxJava 与 Retrofit 结合的最佳实践](https://gank.io/post/56e80c2c677659311bed9841)
 [Retrofit 2.0 + OkHttp 3.0 配置](https://drakeet.me/retrofit-2-0-okhttp-3-0-config)
 [Novate:对Retrofit2.0的又一次完美改进加强！（九）](http://www.jianshu.com/p/d7734390895e)
 (本框架正是借鉴了Novate！)
 
##Introduce：
现在项目需求越来越多，api随之越来越多，本人的项目api数就已达120+，如果都把这些api放入一个ApiService内，加上注释显的格外长，而且不利于查看api。
于是采用第二种，以模块的形式对api进行分类，每个模块下对应若干个api，但是以retrofit的形式需要创建若干个ApiService, 虽然这种方式对于维护很方便，
但是模块数多了，效果也不是很好。对于懒人来说，如果有种统一的方法请求网络数据，你只要传入一个URL，就能回调你所需要的数据，对于这种形式再适合不过。
于是Renovace应运而生。Retrofit英语的意思是改造，Renovace在捷克语言中是改造的意思。

##Advantage
- 统一请求访问网络的流程控制
- 支持自定义Retrofit
- 支持自定义Okhttpclient
- 支持自定义的扩展API

##Usage
###init()
初始化1:Renovace内部会创建一个默认的Retrofit和Okhttpclient。

     Renovace.getInstance().init(baseUrl);
初始化2: Renovace内部会创建一个默认的Retrofit，用户可自定义Okhttpclient。

    Renovace.getInstance().init(baseUrl, new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                HashMap<String, String> headers = new HashMap<>();
                headers.put("keys", "xxxxxxxxxxxxxxxxx");

                return new OkHttpClient.Builder()
                        .addInterceptor(logInterceptor)
                        .addInterceptor(new HeaderInterceptor(headers))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();
            }
        });
初始化3:用户自定义retrofit和okhttpclient

    Renovace.getInstance().init(new IRenovace() {
            @Override
            public Retrofit getRetrofit() {
                return new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .validateEagerly(Utils.DEBUG)
                        .build();;
            }
        });
###BaseBean
Renovace内部是靠RenovaceBean<T> 进行解析的，如果你的数据结构跟RenovaceBean不同，你可以在你的项目中继承RenovaceBean，然后重写getResult和getCode等方法来实现自己的需求。
####eg:
默认的数据结构为:

![default_struct](./image/default_struct.png)

如果你的数据结构是这样的：

![your_struct](./image/your_struct.png)

那么你的basebean可以写成这样


    public class BaiduApiBean<T> extends RenovaceBean<T>{
    int errNum;
    String errMsg;
    T retData;

    @Override
    public int getCode() {
        return errNum;
    }

    @Override
    public void setCode(int code) {
        errNum = code;
    }

    @Override
    public String getError() {
        return errMsg;
    }

    @Override
    public void setError(String error) {
        errMsg = error;
    }

    @Override
    public T getResult() {
        return retData;
    }

    @Override
    public void setResult(T result) {
        this.retData = result;
    }

###get()
get方式提供了4种实现

1，此种方式为Renovace提供了默认回调，附带dialog展示，以及toast提示等，如果想自定义弹出的dialog，你可以实现 IRenovaceCallBack 接口

    Renovace.getInstance().get(String apiUrl, RenovaceCallback<Your Bean>); 

2，Renovace保留了Subscribe接口，用户可在onNext直接拿到解析成功后的bean

    Renovace.getInstance().get(String apiUrl, Subscribe<Your Bean>);

3，如果你想要原始数据你可以传入ResponseBody

    Renovace.getInstance().get(String apiUrl, RenovaceCallbackResponseBody>);
    Renovace.getInstance().get(String apiUrl, Subscribe<ResponseBody>);

4，Renovace还提供了传入参数的接口，实现方式同上面4个

	Renovace.getInstance().get(String apiUrl, Map params, RenovaceCallback<Your Bean>); 
	Renovace.getInstance().get(String apiUrl, Map params, Subscribe<Your Bean>);
	Renovace.getInstance().get(String apiUrl, Map params, RenovaceCallbackResponseBody>);
	Renovace.getInstance().get(String apiUrl, Map params, Subscribe<ResponseBody>);

###post()
Post也提供了与get一样的4种实现方式,具体使用方法你只需将get替换成post即可！！！


###自定义API
Renovace提供了用户自定义ApiService的接口，您只需调用call方法即可
eg:

    public interface TestApi {
    	@GET("app.php")
    	Observable<SouguBean> getSougu(@QueryMap Map<String, String> maps);
	}


	Renovace.getInstance().init("http://lbs.sougu.net.cn/");
    HashMap<String, String> parameters = new HashMap<>();
    parameters.put("m", "souguapp");
    parameters.put("c", "appusers");
    parameters.put("a", "network");

    TestApi testApi = Renovace.getInstance().create(TestApi.class);
    Renovace.getInstance().call(testApi.getSougu(parameters), new RenovaceCallback<SouguBean>(this) {
        @Override
        public <T> void onSuccees(T bean) {
            showToast(bean.toString());
        }
    });


[更多内容>>>](http://blog.csdn.net/u013555324/article/details/52973007)
