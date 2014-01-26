package util;

//import survey.ThreadedSubsetEvaluator;

import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 23/10/12
 * Time: 12:18
 */
public class ThreadPool {
    private int maxThreads = 100;
    private int totalThreads = 250;
    private int waitTime = 5000;
    private ThreadPoolExecutor threadPoolExecutor;
    private volatile double temp = 0d;

    public ThreadPool(int maxThreads, int totalThreads) {
        this.maxThreads = maxThreads;
        this.totalThreads = totalThreads;
        this.start();
    }

    public ThreadPool(int maxThreads) {
        this.maxThreads = maxThreads;        /*this.totalThreads = totalThreads;
		this.waitTime = waitTime;*/
        this.unboundedStart();
    }

    public ThreadPool() {
        this.start();
    }

    public void unboundedStart() {
        //LinkedBlockingQueue
        this.threadPoolExecutor = new ThreadPoolExecutor(maxThreads, maxThreads, 1,
                TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void start() {
        this.threadPoolExecutor = new ThreadPoolExecutor(maxThreads, maxThreads * 2, 1,
                TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(totalThreads, true),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void purge() {
        this.threadPoolExecutor.purge();
    }

    public void waitForCompletion() {
        try {
            while (!threadPoolExecutor.awaitTermination(waitTime, TimeUnit.MILLISECONDS)) {
                //System.out.println("Awaiting completion of threads ... " + threadPoolExecutor.getCompletedTaskCount() + "/" + totalThreads);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public boolean shutdown() {
        threadPoolExecutor.shutdown();
		/*try {
			while (!threadPoolExecutor.awaitTermination(waitTime, TimeUnit.MILLISECONDS)) {
				//System.out.println("Awaiting completion of threads ... " + threadPoolExecutor.getCompletedTaskCount() + "/" + totalThreads);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}*/
		/*while (threadPoolExecutor.getCompletedTaskCount() != totalThreads) {
			try {
				System.out.println("c=" + threadPoolExecutor.getCompletedTaskCount());
				threadPoolExecutor.wait(waitTime);
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/
        threadPoolExecutor.shutdown();
        try {
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPoolExecutor.shutdownNow();
            }
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException ex) {
            threadPoolExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.gc();
        return true;
    }

    public void submit(Runnable task) {
        this.threadPoolExecutor.submit(task);
    }

    public Double submit(Callable task) throws ExecutionException, InterruptedException {
        Future<Double> future = this.threadPoolExecutor.submit(task);
        return future.get();
    }

    /*public Future<Double> submitSubsetEvaluation(ThreadedSubsetEvaluator task) throws ExecutionException, InterruptedException {
        return this.threadPoolExecutor.submit(task);
    }*/

    public Future<int[]> submitEvaluator(Callable task) throws ExecutionException, InterruptedException {
        Future<int[]> future = this.threadPoolExecutor.submit(task);
        return future;
    }

    public synchronized double getTemp() {
        return temp;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void addToTemp(double value) {
        this.temp = temp + value;
    }
}
