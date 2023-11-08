package com.luoli.stock.test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author liluo
 * @create 2023/11/1 9:32
 */
public class ConcurrentTest implements Runnable {
    /**
     * 模拟10个线程并发
     */
    private static CountDownLatch countDownLatch = new CountDownLatch(10);

    /**
     * 分布式分段锁
     */
    private static SegmentDistributeLock segmentDistributeLock = new SegmentDistributeLock();

    /**
     * 购买数量
     */
    private int num;

    /**
     * 会员id
     */
    private String memberId;

    public ConcurrentTest(String memberId, int num) {
        this.num = num;
        this.memberId = memberId;
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        // 模拟并发扣减库存(扣减1-50个)
        for (int i = 0; i < 10; i++) {
            new Thread(new ConcurrentTest("memberId_" + i, random.nextInt(50) + 1), "线程" + i).start();
            countDownLatch.countDown();
        }
        TimeUnit.SECONDS.sleep(5);
        // 并发扣减库存结束,查询最终库存
        System.out.println("-----并发扣减库存结束,查看剩余库存-------");
        System.out.println("-----并发扣减库存结束,查看剩余库存-------");
        System.out.println("-----并发扣减库存结束,查看剩余库存-------");
        segmentDistributeLock.showStocks();
        segmentDistributeLock.shutDown();
    }

    @Override
    public void run() {
        try {
            StockRequest request = new StockRequest(this.memberId, this.num);
            // 在此阻塞,等到计数器归零之后,再同时开始 扣库存
            System.out.println(Thread.currentThread().getName() + "已到达, 即将开始扣减库存: "+ this.num);
            countDownLatch.await();
            if(segmentDistributeLock.handlerStock_02(request)){
                System.out.println(Thread.currentThread().getName() + " 扣减成功, 扣减库存为: " + this.num);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
