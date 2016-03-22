/**
 * 
 */
package org.sysma.abc.core.Examples.PublishSubscribe;

import java.util.*;

public class x {

    public static void main(String[] args) {

        ThreadManager<Thread> t = new ThreadManager<Thread>();
        Thread a = new MyThread(t);
        Thread b = new MyThread(t);     
        Thread c = new MyThread(t);
        t.add(a);
        t.add(b);
        t.add(c);
        a.start();
       // a.interrupt();
        b.start();
        c.start(); 
        
    }
}

class ThreadManager<T> extends ArrayList<T> {

    public void stopThreads() {
        for (T t : this) {
            Thread thread = (Thread) t;
            if (thread.isAlive()) {
                try { thread.interrupt(); } 
                catch (Exception e) {/*ignore on purpose*/}
            }
        }
    }
}

class MyThread extends Thread {

    static boolean signalled = false;

    private ThreadManager m;

    public MyThread(ThreadManager tm) {
        m = tm;
    }

    public void run() {
        try {           
            // periodically check ...
            if (this.interrupted()) throw new InterruptedException();
            System.out.println("hi");
            // do stuff
        } catch (Exception e) {
            synchronized(getClass()) {
                if (!signalled) {
                    signalled = true;
                    m.stopThreads();
                }
            }
        }
    }
}
