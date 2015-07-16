package com.vspglobal.ipa.jaxrs.hystrix.plugin;

import com.netflix.hystrix.*;
import com.netflix.hystrix.contrib.servopublisher.HystrixServoMetricsPublisher;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisherCollapser;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisherCommand;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisherThreadPool;

/**
 * Created by casele on 4/17/15.
 */
public class HystrixMetricsPublisherServo extends HystrixMetricsPublisher{

    HystrixMetricsPublisher delegate = HystrixServoMetricsPublisher.getInstance();
    @Override
    public HystrixMetricsPublisherCommand getMetricsPublisherForCommand(HystrixCommandKey commandKey, HystrixCommandGroupKey commandGroupKey, HystrixCommandMetrics metrics, HystrixCircuitBreaker circuitBreaker, HystrixCommandProperties properties) {
        return delegate.getMetricsPublisherForCommand(commandKey, commandGroupKey, metrics, circuitBreaker, properties);
    }

    @Override
    public HystrixMetricsPublisherThreadPool getMetricsPublisherForThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolMetrics metrics, HystrixThreadPoolProperties properties) {
        return delegate.getMetricsPublisherForThreadPool(threadPoolKey, metrics, properties);
    }

    @Override
    public HystrixMetricsPublisherCollapser getMetricsPublisherForCollapser(HystrixCollapserKey collapserKey, HystrixCollapserMetrics metrics, HystrixCollapserProperties properties) {
        return delegate.getMetricsPublisherForCollapser(collapserKey, metrics, properties);
    }
}
