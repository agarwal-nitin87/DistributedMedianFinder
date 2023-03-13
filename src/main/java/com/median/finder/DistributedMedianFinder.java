package main.java.com.median.finder;

import lombok.AllArgsConstructor;
import main.java.com.median.finder.machine.MachineTask;
import main.java.com.median.finder.reducer.Reducer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@AllArgsConstructor
public class DistributedMedianFinder {
    private final String filePath;

    private final Reducer reducer = Reducer.INSTANCE.getInstance();

    public double findMedian() {
        List<MachineTask> machineTaskList = distributeLoad();
        /**
         * Start all threads to find median.
         */
        ExecutorService executor = Executors.newFixedThreadPool(machineTaskList.size());
        try {
            List<Future<Boolean>> listOfFutures = executor.invokeAll(machineTaskList);
            executor.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /**
         * return the median from main.java.finder.reducer.
         */
        return reducer.getMedian();
    }

    private List<MachineTask> distributeLoad() {
        List<MachineTask> machineTaskList = new ArrayList<>();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(new FileInputStream(this.filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                File f = File.createTempFile("Machine", ".csv");
                FileWriter writer = new FileWriter(f);
                writer.write(line);
                writer.close();
                MachineTask task = new MachineTask(f.getAbsolutePath());
                machineTaskList.add(task);
            }
        } catch (Exception e) {
            System.out.println("Failed while distributing work loads to different machines" +  e.getMessage());
        }
        return machineTaskList;
    }


}
