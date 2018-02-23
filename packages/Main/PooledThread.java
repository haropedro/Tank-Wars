/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.logging.Level;
import java.util.logging.Logger;


public class PooledThread extends Thread{
    private ThreadID threadID = new ThreadID(1);
    private ThreadPool pool;
    public PooledThread(ThreadPool pool) {
        super(pool, "GameThreadPool");
        this.pool = pool;
    }
    
    @Override
    public void run() {
        while(!isInterrupted()) {
            Runnable task = null;
            try {
                task = pool.getTask();
            } catch (InterruptedException ex) {
                Logger.getLogger(PooledThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(task == null) {
                return;
            }
            try {
                task.run();
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
