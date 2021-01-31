package anchor.thread.pool;

import anchor.thread.pool.base.CustomFactory;
import anchor.thread.util.CommonUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * 为什么使用线程池？
 *  1.降低资源消耗。重复利用已创建的线程，降低频繁新建、销毁线程的消耗。
 *  2.提高响应速度。不需要创建线程，可直接执行任务。
 *  3.便于监控和调优。
 *
 * 七个参数：
 *  1.int corePoolSize —— 核心线程数
 *  2.int maximumPoolSize —— 最大线程数
 *  3.long keepAliveTime —— 空闲线程(超过 corePoolSize 的线程)的最大存活时间
 *  4.TimeUnit unit —— keepAliveTime参数的时间单位
 *  5.BlockingQueue<Runnable> workQueue —— 存放任务的阻塞队列，常用的阻塞队列有
 *    ArrayBolckingQueue、LinkedBlockingQueue、SynchronousQueue、PriorityQueue
 *  6.ThreadFactory threadFactory —— 创建线程的工厂
 *  7.RejectedExecutionHandler handler —— 拒绝策略，当线程池中线程数超过 maximumPoolSize 时，对新添加的任务执行拒绝策略。
 *    ThreadPoolExecutor 中有四种拒绝策略 CallerRunsPolicy、AbortPolicy、DiscardPolicy、DiscardOldestPolicy。
 *    也可以自己实现 RejectedExecutionHandler 接口来自定义拒绝策略。
 *
 * 线程池的工作流程：
 *  当新的任务被提交到线程池后执行以下三步：
 *   1.当前线程池中线程的数量 == corePoolSize? 执行第 2 步: 创建新的线程来执行任务
 *   2.workQueue 是否已满? 执行第 3 步: 将任务放在 workQueue
 *   3.当前线程池中线程的数量 == maximumPoolSize? 对此任务执行拒绝策略: 创建新的线程来执行任务
 */
public class Pool {

    public static void main(String[] args) {
        System.out.println(CommonUtil.getThreadPid());
    }

    private static ExecutorService init(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,
                100,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new CustomFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        return null;
    }
}
