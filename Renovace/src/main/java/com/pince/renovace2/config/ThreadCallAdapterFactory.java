package com.pince.renovace2.config;

import com.pince.renovace2.thread.RenovaceThread;
import com.pince.renovace2.thread.ThreadStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author athoucai
 * @date 2018/9/11
 */

class ThreadCallAdapterFactory extends CallAdapter.Factory {

    private RxJava2CallAdapterFactory rxFactory = RxJava2CallAdapterFactory.create();

    public ThreadCallAdapterFactory() {
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter callAdapter = rxFactory.get(returnType, annotations, retrofit);
        return callAdapter != null ? new ThreadCallAdapter(callAdapter, annotations) : null;
    }

    final class ThreadCallAdapter<R> implements CallAdapter<R, Observable<?>> {
        CallAdapter<R, Observable<?>> delegateAdapter;
        private ThreadStrategy subscribeScheduler = ThreadStrategy.IO;
        private ThreadStrategy observerScheduler = null;

        ThreadCallAdapter(CallAdapter<R, Observable<?>> delegateAdapter, Annotation[] annotations) {
            this.delegateAdapter = delegateAdapter;
            for (Annotation annotation : annotations) {
                if (annotation instanceof RenovaceThread) {
                    RenovaceThread renovaceThread = (RenovaceThread) annotation;
                    subscribeScheduler = renovaceThread.subscribeThread();
                    observerScheduler = renovaceThread.observeThread();
                    return;
                }
            }
        }

        @Override
        public Type responseType() {
            return delegateAdapter.responseType();
        }

        @Override
        public Observable<?> adapt(Call<R> call) {
            Observable<?> observable = delegateAdapter.adapt(call);
            if (subscribeScheduler != null) {
                observable = observable.subscribeOn(subscribeScheduler.cheduler());
            }
            if (observerScheduler != null) {
                if (observerScheduler == subscribeScheduler) {

                } else {
                    observable = observable.observeOn(observerScheduler.cheduler());
                }
            }
            return observable;
        }
    }
}
