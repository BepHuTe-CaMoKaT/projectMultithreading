package main;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class task2 {
    private static Semaphore s1 = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {

    }
}

class FizzBuzzDemo {

    FizzBuzz fizzBuzz = new FizzBuzz(15);


    public void method() throws InterruptedException {

        try {
            Thread t1 = new Thread(fizzBuzz::fizz).start();
            Thread t2 = new Thread(fizzBuzz::buzz).start();
            Thread t3 = new Thread(fizzBuzz::fizzbuzz).start();
            Thread t4 = new Thread(fizzBuzz::number).start();
            Thread.sleep(2000);
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }
    }


}

class FizzBuzz {
    private int number;

    public FizzBuzz(int n) {
        this.number = n;
    }

    public void fizz(Runnable printFizz) throws InterruptedException {
    }

    public void buzz(Runnable printBuzz) {
    }

    public void fizzbuzz(Runnable printFizzBuzz) {
    }

    public void number(Runnable printNumber) {
    }

}
