package vn.com.phanbagiang.myapplication.events;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by giangphanba on 10/10/2021.
 */
public class MyWorker extends Worker {

    private static final String TAG = "MyWorker";


    public MyWorker(@NonNull  Context context, @NonNull  WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork for Sync");
//        Observable<Integer> o = Observable.range(0, getInputData().getInt("GIANG_NUM", 5))
//
//                .subscribeOn(Schedulers.io());
//        o.subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
//                try {
//                    Thread.sleep(1000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Log.d(TAG, "onNext: "+integer+Thread.currentThread().getName());
//            }
//
//            @Override
//            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        for(int i=0; i< 100; i++){
            try {
                Log.d(TAG, "doWork: "+i);
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return Result.success();
    }
}
