package com.vspglobal.ipa.jaxrs.hystrix.plugin;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by casele on 4/16/15.
 */
public class HystrixConcurrencyStrategyDaemonThread extends HystrixConcurrencyStrategy {

    public ThreadPoolExecutor getThreadPool(final HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return new ThreadPoolExecutor(((Integer)corePoolSize.get()).intValue(), ((Integer)maximumPoolSize.get()).intValue(), (long)((Integer)keepAliveTime.get()).intValue(), unit, workQueue, new ThreadFactory() {
            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "hystrix-" + threadPoolKey.name() + "-" + this.threadNumber.incrementAndGet());
                t.setDaemon(true);
                return t;
            }
        });
    }

}
