package com.example.todolist;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    public static AppExecutor executorInstance;
    private final Executor singleThreadExecutor;

    // Making constructor private to apply singleton pattern
    private AppExecutor(Executor singleThreadExecutor) {
        this.singleThreadExecutor = singleThreadExecutor;
    }

    // returns same instance every time this class is called by app
    public static AppExecutor getInstance() {
        if (executorInstance == null) {
            synchronized (AppExecutor.class) {
                if (executorInstance == null) {
                    // executor that runs submitted task sequentially
                    executorInstance = new AppExecutor(Executors.newSingleThreadExecutor());
                }
            }
        }
        return executorInstance;
    }

    // simple getter method that return Executor which run submitted runnable tasks subsequently
    public Executor getSingleThreadExecutor() {
        return singleThreadExecutor;
    }
}
