package main.java.com.median.finder.machine;

import main.java.com.median.finder.routing.InMemoryRoutingService;
import main.java.com.median.finder.routing.RoutingService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;


/**
 * Simulates a  main.java.finder.machine on a distributed system.
 * It exposes RPC callback for other machines to connect.
 *
 */
public class MachineTask  implements Callable<Boolean> {


    /**
     * Assumes every main.java.finder.machine is storing around 100 GB of data which is close to 1 billion integers
     * considering they are stored in char form with commas.
     *
     * Stores around 100 MB of data in main memory before flushing it the dispatcher.
     *
     * Per record (32 bits + 64 bits) , 1KB = 80 records,
     * 1M records = 12MB
     */
    private static final int MAXIMUM_SIZE = 1000000;

    private static final Random r = new Random();

    private static final RoutingService routingService = new InMemoryRoutingService();

    private final String filePath;

    Map<Integer, Long> freqMap = new HashMap<>();

    public MachineTask(final String filePath) {
        this.filePath = filePath;
    }

    public void run() {
        try (Scanner sc = new Scanner(new File(filePath));) {
            sc.useDelimiter(",");
            /**
             * Have some randomness in the records that is being read before flushing it to corresponding machines.
             * The jitter is to reduce thundering herd where all hosts connect to main.java.finder.routing service at the same time.
             */
            int maxRecordsToRead = MAXIMUM_SIZE + r.nextInt(MAXIMUM_SIZE);
            int counter=0;
            while(sc.hasNext()) {
                String parsedInteger = sc.next().trim();
                int value = Integer.valueOf(parsedInteger);
                freqMap.put(value, freqMap.getOrDefault(value, 0L)+1);
                counter++;
                /**
                 * Flush the frequency map via main.java.finder.routing service to corresponding machines.
                 */
                if (counter > maxRecordsToRead) {
                    routingService.updateCounters(freqMap);
                    freqMap.clear();
                }


            }
            routingService.updateCounters(freqMap);
            freqMap.clear();
        } catch (final FileNotFoundException e) {

        }
    }

    @Override
    public Boolean call() throws Exception {
        this.run();
        return true;
    }
}
