package com.example.productservice.process;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class TaskManager {
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private Long maxTimeout = 10000L;

    public void execute(List<BusinessProcess> tasks) {

        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        Long timeStart = System.currentTimeMillis(); // số?
        List<Future<Runnable>> list = new ArrayList<>();
        for (BusinessProcess task : tasks) {
            Future f = executorService.submit(task);
            list.add(f);
        }

        while(true) {
            boolean finishAllTask = true;
            for(Future f : list) {
                finishAllTask = finishAllTask && f.isDone();
            }
            if (System.currentTimeMillis() - timeStart > maxTimeout) {
                finishAllTask = true;
            }
            if (finishAllTask) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
