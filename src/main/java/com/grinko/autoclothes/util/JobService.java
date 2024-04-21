package com.grinko.autoclothes.util;

import java.time.Duration;
import java.util.List;

public interface JobService {

    ScheduledJob schedule(String jobName,
                          Runnable run,
                          Duration delay);

    ScheduledJob schedule(String jobName, List<Runnable> runs, Duration delay);

    /**
     * Job
     */
    interface ScheduledJob {

        /**
         * cancel
         */
        void cancel();
    }

}