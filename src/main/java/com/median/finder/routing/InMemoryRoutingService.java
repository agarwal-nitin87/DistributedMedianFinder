package main.java.com.median.finder.routing;

import main.java.com.median.finder.reducer.Reducer;

import java.util.HashMap;
import java.util.Map;


/**
 * Stores all thread information and routes the counters in form of hashmap to appropriate main.java.finder.machine.
 *
 * Total integers are 5 billion and every record of int=>freq is 100 bits -> 12.5bytes.
 * ~ 60 billion bytes -> 60 * 1K * 1K * 1K = 60GB
 * So, to store all 5 billion integers frequency requires around
 * 60 GB of memory.
 *
 * So, a single host can store all integer frequencies easily
 *
 */

/**
 * It is a mediator between main.java.finder.reducer and individual machines.
 * Since, individual machines can grow to a large number(10K). Connecting all 10K machines, to a single main.java.finder.reducer
 * will overload reducers.
 *
 * Number of routers ~ Number of machines / 1000K
 * Every main.java.finder.machine pass around 20MB of data per call, which add 20GB of buffer data in the in memory main.java.finder.routing service
 * before flushing to the main.java.finder.reducer node.
 */
public class InMemoryRoutingService implements RoutingService {

    private final Map<Integer, Long> freqMap = new HashMap<>();

    private final Reducer reducer = Reducer.INSTANCE.getInstance();
    @Override
    public void  updateCounters(Map<Integer, Long> counterMap) {
        for (Map.Entry<Integer, Long> entry : counterMap.entrySet()) {
            freqMap.put(entry.getKey(), freqMap.getOrDefault(entry.getKey(), 0L)+entry.getValue());
        }
        reducer.updateCounters(freqMap);
        freqMap.clear();
    }
}
