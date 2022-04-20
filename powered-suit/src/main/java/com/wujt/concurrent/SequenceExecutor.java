package com.wujt.concurrent;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author wujt
 */
@Slf4j
public class SequenceExecutor implements Executor {

    /**
     * 代理的executor
     */
    private final Executor delegateExecutor;
    // 顺序线程池队列
    private final LinkedBlockingQueue<Runnable> processQueue;
    /**
     * 顺序工作者（线程）
     */
    private final SequenceWorker worker;

    public SequenceExecutor(Executor delegateExecutor, int queueSize) {
        this.delegateExecutor = delegateExecutor;
        this.processQueue = new LinkedBlockingQueue<>(queueSize);
        this.worker = new SequenceWorker(processQueue);
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        //提交任务入队
        if (!processQueue.offer(runnable)) {
            throw new RejectedExecutionException("SequenceExecutor queue is full.");
        }
        //尝试启动队列任务,double check
        if (worker.getState() == WorkerState.IDLE
                || worker.getState() == WorkerState.PRE_IDLE) {
            synchronized (worker) {
                //二次检查工作者状态与队列内任务数量
                if (worker.getState() == WorkerState.IDLE
                        && processQueue.size() > 0) {
                    worker.setState(WorkerState.RUNNING);
                    delegateExecutor.execute(worker);
                }
            }
        }
    }

    enum WorkerState {
        // 线程空闲
        IDLE,
        /**
         * 预处理空闲,
         */
        PRE_IDLE,
        /**
         * 运行状态
         */
        RUNNING
    }

    private final class SequenceWorker implements Runnable {
        // 线程队列
        private final LinkedBlockingQueue<Runnable> processQueue;

        SequenceWorker(LinkedBlockingQueue<Runnable> processQueue) {
            this.processQueue = processQueue;
            this.state = WorkerState.IDLE;
        }

        // 线程状态
        private volatile WorkerState state;

        WorkerState getState() {
            return state;
        }

        void setState(WorkerState state) {
            this.state = state;
        }

        @Override
        public void run() {
            boolean interruptedDuringTask = false;
            try {
                while (true) {
                    Runnable task = processQueue.poll();
                    if (task == null) {
                        //设定将停止工作,状态将标识新的task由存在worker优先处理
                        setState(WorkerState.PRE_IDLE);
                        synchronized (worker) {
                            //double poll
                            task = processQueue.poll();
                            if (task == null) {
                                //仍然没任务则停止工作
                                setState(WorkerState.IDLE);
                                // 线程工作完成
                                return;
                            } else {
                                //已存在,设定继续开始工作
                                setState(WorkerState.RUNNING);
                            }
                        }
                    }
                    //清理并记录线程interrupted位
                    interruptedDuringTask |= Thread.interrupted();
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        log.error("Exception while executing runnable " + task, e);
                    }
                }
            } catch (Error error) {
                log.error("SequenceWorker error happen ", error);
            } finally {
                setState(WorkerState.IDLE);
                if (interruptedDuringTask) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
