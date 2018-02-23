
package Main;

import java.util.LinkedList;
import java.util.List;


public class ThreadPool extends ThreadGroup{
    private static ThreadID threadID = new ThreadID(1);
    private boolean alive;
    private List<Runnable> taskList;

    public ThreadPool(int numThreads) {
        super("GameThreadPool - " + threadID.next() );
        taskList = new LinkedList<>();
        setDaemon(true);//exits thread group when GamePanel exits
        alive = true;
        for(int i = 0; i < numThreads; i++) {
            new PooledThread(this).start();
        }
    }
    
    public synchronized void runTask(Runnable task) {
        if(!alive) throw new IllegalStateException("ThreadPool is dead");
        if(task != null) {
            taskList.add(task);
            notify();
        }
    }
    
    public synchronized void closeTask() {
        if(!alive) return;
        alive = false;
        taskList.clear();
        interrupt();
    }
    
    public void join() {
        synchronized(this) {//runs all active threads
            alive = false;
            notifyAll();
        }
        
        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        
        for(int i = 0; i < count; i++) {
            try {
                threads[i].join();
            }
            catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
    
    public synchronized Runnable getTask() throws InterruptedException {
        while(taskList.isEmpty()) {
            if(!alive) {
                return null;
            }
            wait();
        }
        return taskList.remove(0);
    }
    
}
