package com.testMaven;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

/**
 * @author athoucai
 * @date 2018/8/17
 */

public class TestMain {

    public static void main(String[] args) throws InterruptedException {
//        PublishSubject<Integer> subject = PublishSubject.create();
//        subject.onNext(1);
//        subject.subscribe(System.out::println);
//        subject.onNext(2);
//        subject.onNext(3);
//        subject.onNext(4);

//        ReplaySubject<Integer> s = ReplaySubject.create();
//        s.subscribe(v -> System.out.println("Early:" + v));
//        s.onNext(0);
//        s.onNext(1);
//        s.subscribe(v -> System.out.println("Late: " + v));
//        s.onNext(2);

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                ReplaySubject<Integer> s = ReplaySubject.createWithTime(150, TimeUnit.MILLISECONDS,
//                        Schedulers.computation());
//                s.onNext(0);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                s.onNext(1);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                s.onNext(2);
//                s.subscribe(v -> System.out.println("Late: " + v));
//                s.onNext(3);
//            }
//        }.start();

        ReplaySubject<Integer> s = ReplaySubject.createWithTime(150, TimeUnit.MILLISECONDS,
                Schedulers.single());
        s.onNext(0);
        Thread.sleep(100);
        s.onNext(1);
        Thread.sleep(100);
        s.onNext(2);
        s.subscribe(v -> System.out.println("Late: " + v));
        s.onNext(3);
    }
}
