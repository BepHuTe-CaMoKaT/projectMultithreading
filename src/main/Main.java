package main;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Foo2 f = new Foo2();
        Thread t1 = new Thread();
        Thread t2 = new Thread();
        Thread t3 = new Thread();
        CompletableFuture.runAsync(()->f.first(t1));
        Thread.sleep(1000);
        CompletableFuture.runAsync(()-> {
            try {
                f.second(t2);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CompletableFuture.runAsync(()->{
            try {
                f.third(t3);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


    }
}

class Foo2{
    private static Semaphore s1 = new Semaphore(0);
    private static Semaphore s2 = new Semaphore(0);

    void first(Runnable printFirst){
        printFirst.run();
        System.out.print("first");
        s1.release();
    }
    void second(Runnable printSecond) throws InterruptedException{
        s1.acquire();
        printSecond.run();
        System.out.print("second");
        s2.release();
    }
    void third(Runnable printThird) throws InterruptedException{
        s2.acquire();
        printThird.run();
        System.out.println("third");
        s2.release();
    }
}
class Foo{
    public int threadNumber;

    public Foo() {
        this.threadNumber = 1;
    }

    synchronized void first(Runnable printFirst) throws InterruptedException {
        while (threadNumber!=1) wait();
        printFirst.run();
        threadNumber++;
        notifyAll();
    }
    synchronized void second(Runnable printSecond) throws InterruptedException {
        while (threadNumber!=2) wait();
        printSecond.run();
        notifyAll();
    }
    synchronized void third(Runnable printThird) throws InterruptedException {
        while (threadNumber!=3) wait();
        printThird.run();
        threadNumber++;
        notifyAll();
    }
}
class Foo1{
    public int threadNumber;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public Foo1() {
        this.threadNumber = 1;
    }

    public void first(Runnable printFirst) throws InterruptedException{
        lock.lock();
        try {
            printFirst.run();
            this.threadNumber++;
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException{
        lock.lock();
        try {
            while (threadNumber!=2) condition.await();
            printSecond.run();
            this.threadNumber++;
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    public void third(Runnable printThird)throws InterruptedException{
        lock.lock();
        try {
            while (threadNumber!=3) condition.await();
            printThird.run();
            this.threadNumber++;
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
}
