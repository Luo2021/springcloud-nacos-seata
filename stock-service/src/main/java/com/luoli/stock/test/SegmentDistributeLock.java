package com.luoli.stock.test;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.stream.Stream;

/**
 * @version 1.0.0
 * @@menu <p>
 * @date 2021/6/10 14:33
 */
public class SegmentDistributeLock {
    /**
     * 使用redis分布式锁扣减库存,弊端: 请求量大的话,会导致吞吐量降低
     * 优化: 分段锁并发扣减库存
     * 将表中的库存字段 分为 5个库存字段, 然后导入redis,库存预热, 然后参考ConcurrentHashMap的分段锁思想
     * 来一个请求后,对库存字段 加 分段锁, 分段锁扣减库存
     * 如果当前分段锁库存不够,就扣减掉当前的库存,然后去锁下一个分段锁,扣减库存
     * <p>
     * git: https://gitee.com/easybao/segmentDistributeLock.git
     * 依赖jar包:
     * <dependency>
     * <groupId>org.redisson</groupId>
     * <artifactId>redisson</artifactId>
     * <version>3.13.5</version>
     * </dependency>
     */
    RedissonClient redissonClient;
    RBucket<RedisStock[]> bucket;
    private ThreadLocal<StockRequest> threadLocal = new ThreadLocal<>();
    static volatile RedisStock[] redisStocks;
    private final int beginTotalNum; //初始总库存,避免并发过程中 调用getCurrentTotalNum()获取到的总库存发生变化

    public SegmentDistributeLock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.211.128:6379");
        this.redissonClient = Redisson.create(config);

        redisStocks = new RedisStock[5];
        redisStocks[0] = new RedisStock("pId_stock_00", 20);
        redisStocks[1] = new RedisStock("pId_stock_01", 20);
        redisStocks[2] = new RedisStock("pId_stock_02", 20);
        redisStocks[3] = new RedisStock("pId_stock_03", 20);
        redisStocks[4] = new RedisStock("pId_stock_04", 20);
        // 初始总库存
        this.beginTotalNum = getCurrentTotalNum();

        // 库存预热,存到redis中 ,  这里没有采用因为将库存预热存到redis中,取出来的时候,解析异常, 不想花时间解决,所以将库存预热 变成一个类变量
//        bucket = redissonClient.getBucket("pId_stock");
//        bucket.set(redisStocks);

    }

    public RedissonClient getRedissonClient() {
        return this.redissonClient;
    }

    public int getCurrentTotalNum() {
        // 获取实时总库存
        return Stream.of(redisStocks).mapToInt(RedisStock::getNum).sum();
    }


    /**
     * 使用redis分布式锁扣减库存,弊端: 请求量大的话,会导致吞吐量降低
     * 优化: 分段锁并发扣减库存
     * 将表中的库存字段 分为 5个库存字段, 然后导入redis,库存预热, 然后参考ConcurrentHashMap的分段锁思想
     * 来一个请求后,对库存字段 加 分段锁, 分段锁扣减库存
     * 如果当前分段锁库存不够,就扣减掉当前的库存,然后去锁下一个分段锁,扣减库存
     *
     * @param request
     * @return
     */
    public boolean handlerStock_02(StockRequest request) {
        // 先做校验: 判断扣减库存 是否比 初始总库存还大,是的话就直接false,  避免无限循环扣减不了
        if (request.getBuyNum() > this.beginTotalNum) {
            return false;
        }
        // 使用本地线程变量保存请求,确保参数只在本线程使用
        threadLocal.set(request);

        // 这里使用 ThreadLocal代码逻辑和ConcurrentHashMap的分段锁
        RedissonClient redissonClient = getRedissonClient();
        RedisStock[] tab = redisStocks;
        int len = tab.length;
        int i = (request.getMemberId().hashCode() < 0 ? 0 : request.getMemberId().hashCode()) % len;

        for (RedisStock e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {

            RLock segmentLock = null;
            try {
                // 2: 对该元素加分布式分段锁
                segmentLock = redissonClient.getLock(e.getStockName());
                segmentLock.lock();

                int buyNum = threadLocal.get().getBuyNum();
                if (buyNum <= e.getNum()) {
                    //扣减库存
                    e.setNum(e.getNum() - buyNum);
                    // 扣减成功后,跳出循环,返回结果
                    return true;
                } else {
                    // 如果并发过程中获取到总库存<= 0 说明已经没有库存了,  如果当前需要扣减的库存 > 此时总库存就返回false,扣件失败
                    if (getCurrentTotalNum() <= 0 || threadLocal.get().getBuyNum() > getCurrentTotalNum()) {
                        // 没有库存就false
                        System.out.println(Thread.currentThread().getName() + " 扣减库存数: " + threadLocal.get().getBuyNum() + "失败" + "   此时总库存为: " + getCurrentTotalNum());
                        return false;
                    }
                    // 扣减掉当前的 分段锁对应的库存,然后对下一个元素加锁
                    threadLocal.get().setBuyNum(buyNum - e.getNum());
                    e.setNum(0);
                }
            } finally {
                // 3: 解锁
                segmentLock.unlock();
            }
        }
        threadLocal.remove();
        return false;
    }

    private static int nextIndex(int i, int len) {
        return ((i + 1 < len) ? i + 1 : 0);
    }

    public static int FNVHash(String key) {
        final int p = 16777619;
        Long hash = 2166136261L;
        for (int idx = 0, num = key.length(); idx < num; ++idx) {
            hash = (hash ^ key.charAt(idx)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash.intValue();
    }

    // 显示redis中的库存
    public void showStocks() {
        for (RedisStock redisStock : redisStocks) {
            System.out.println(redisStock);
        }
    }

    public void shutDown() {
        this.redissonClient.shutdown();
    }


}



