package main.java.com.median.finder.reducer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The final main.java.finder.reducer class which stores the integers and there frequencies.
 *  * Total integers are 5 billion and every record of int=>freq is 100 bits -> 12.5bytes.
 *  * ~ 60 billion bytes -> 60 * 1K * 1K * 1K = 60GB
 *  * So, to store all 5 billion integers frequency requires around
 *  * 60 GB of memory.
 *  *
 *  * So, a single host can store all integer frequencies easily.
 *  For simplicity here we are assuming the dataset is such that it fits in memory.
 *  But, it is more prudent to store it in file/ database for fault tolerant cases
 *  (For ex: process starts, or main.java.finder.machine goes away)
 *
 *  Also, it assumes all integers are positive in the range of 1000000, so that the program can finish in reasonable time.
 *
 */
public enum Reducer {

    INSTANCE;

    private static final Map<Integer, Long> freqMap = new ConcurrentHashMap<>();

    private static final int MIN_VALUE=1;

    private static final int MAX_VALUE=1000000;

    private  Long totalSize;

    private Reducer() {
    }

    public  Reducer getInstance() {
        return INSTANCE;
    }

    public void updateCounters(Map<Integer, Long> counterMap) {

        for (Map.Entry<Integer, Long> entry : counterMap.entrySet()) {
            freqMap.put(entry.getKey(), freqMap.getOrDefault(entry.getKey(), 0L)+entry.getValue());
        }

    }

    public double getMedian() {
        int scannedNumbers = 0;
        calculateTotalSize();
        long midIndex1 = (totalSize+1)/2;
        long midIndex2 = midIndex1+1;
        Integer midElement1 = null;
        Integer midElement2 = null;
        for (int i=MIN_VALUE;i<=MAX_VALUE;i++) {
            if (freqMap.containsKey(i)) {
                scannedNumbers += freqMap.get(i);
                if (scannedNumbers >=midIndex1 && midElement1 == null) {
                    midElement1 = i;
                }
                if (scannedNumbers >=midIndex2) {
                    midElement2 = i;
                    // no longer need to process further
                    break;
                }
            }
        }

        if (totalSize %2 ==0) {
            return ((double) (midElement1 + midElement2)) / 2.0;
        } else {
            return (double) midElement1;
        }
    }

    private void calculateTotalSize() {
        long totalSize = 0L;
        for (int i=MIN_VALUE;i<=MAX_VALUE;i++) {
            if (freqMap.containsKey(i)) {
                totalSize += freqMap.get(i);
            }
        }
        this.totalSize = totalSize;
    }
}
