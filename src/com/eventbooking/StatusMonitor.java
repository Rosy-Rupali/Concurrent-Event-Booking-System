package com.eventbooking;

public class StatusMonitor implements Runnable{
    private volatile boolean running = true;
    private final long intervalMillis;
    public StatusMonitor(long intervalMillis) {
        if(intervalMillis <=0){
            throw new IllegalArgumentException("Monitoring interval must be positive.");
        }
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run(){
        while (running){
            try{
                Thread.sleep(intervalMillis);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public void stopMonitoring(){
        running = false;
    }
}
