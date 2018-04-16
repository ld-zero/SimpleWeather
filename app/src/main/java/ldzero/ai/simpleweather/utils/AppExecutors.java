package ldzero.ai.simpleweather.utils;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private Executor mDiskIO;

    private Executor mNetworkIO;

    private Executor mMainThread;

    public AppExecutors() {
        mDiskIO = Executors.newSingleThreadExecutor();
        mNetworkIO = Executors.newFixedThreadPool(3);
        mMainThread = new MainThreadExecutor();
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mHandler = new Handler();

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }
}
