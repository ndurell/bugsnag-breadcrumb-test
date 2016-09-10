package com.noahdurell.bugsnagbreadcrumbs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bugsnag.android.Bugsnag;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bugsnag.init(this);
        Bugsnag.leaveBreadcrumb("onCreate()");
        setContentView(R.layout.activity_main);
        Bugsnag.notify(new RuntimeException("Non-fatal"));
        Button crashButton = (Button) findViewById(R.id.crash_button);
        crashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bugsnag.leaveBreadcrumb("onClick()");
                Observable.fromCallable(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        if (true) {
                            throw new RuntimeException("click and crashed.");
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io()).subscribe();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bugsnag.leaveBreadcrumb("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bugsnag.leaveBreadcrumb("onResume()");
        Observable.interval(0, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Bugsnag.leaveBreadcrumb("breadcrumb " + aLong);
                    }
                });
    }

}
