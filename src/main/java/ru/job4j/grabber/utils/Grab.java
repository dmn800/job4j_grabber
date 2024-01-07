package ru.job4j.grabber.utils;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.io.IOException;

public interface Grab {
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException, IOException;
}
