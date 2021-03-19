package main;

import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.function.IntConsumer;



public class task2 {
    public static void main(String[] args) {
        FizzBuzzDemo.process(new FizzBuzz(15));
    }
}

class FizzBuzzDemo {

    public static String process(FizzBuzz fizzBuzz){
        ExecutorService e = Executors.newFixedThreadPool(4);
        final LinkedList<Future<?>> futures = new LinkedList<>();
        final CopyOnWriteArrayList<String> out = new CopyOnWriteArrayList<>();

        futures.add(e.submit(()->{
            try {
                fizzBuzz.fizz(()->out.add("fizz"));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }));

        futures.add(e.submit(()->{
            try {
                fizzBuzz.buzz(()->out.add("buzz"));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }));

        futures.add(e.submit(()->{
            try {
                fizzBuzz.fizzBuzz(()->out.add("fizzBuzz"));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }));

        futures.add(e.submit(()->{
            try {
                fizzBuzz.number(x->out.add(""+x));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }));

        for (Future<?> f:futures){
            try {
                f.get(700, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                ex.printStackTrace();
            }
        }

        return out.toString();
    }
}

class FizzBuzz {
    private int n;
    private int count;


    public FizzBuzz(int n) {
        this.n = n;
        this.count = n;
    }
    Semaphore fizz = new Semaphore(0);
    Semaphore buzz = new Semaphore(0);
    Semaphore fizzBuzz = new Semaphore(0);
    Semaphore number = new Semaphore(1);
    public void fizz(Runnable printFizz)throws InterruptedException {
        while (count>0){
            fizz.acquire();
            if (count==0) break;
            count--;
            printFizz.run();
            number.release();
        }
    }
    public void buzz(Runnable printBuzz) throws InterruptedException{
        while (count>0){
            buzz.acquire();
            if (count==0) break;
            count--;
            printBuzz.run();
            number.release();
        }
    }
    public void fizzBuzz(Runnable printFizzBuzz) throws InterruptedException{
        while (count>0){
            fizzBuzz.acquire();
            if (count==0) break;
            count--;
            printFizzBuzz.run();
            number.release();
        }
    }

    public void number(IntConsumer printNumber) throws InterruptedException{
        while (count!=0){
            number.acquire();
            if ((n-count+1)%5==0&&(n-count+1)%3==0){
                fizzBuzz.release();
                continue;
            }
            if ((n-count+1)%5==0){
                buzz.release();
                continue;
            }
            if ((n-count+1)%3==0){
                fizz.release();
                continue;
            }
            number.release();
            printNumber.accept(n-count+1);
            count--;
        }
        fizz.release();
        buzz.release();
        fizzBuzz.release();
    }
}
