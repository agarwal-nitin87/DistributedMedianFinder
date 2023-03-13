package main.java.com.median.finder.routing;

import java.util.Map;

/**
 * A middleware to store the intermediate frequency of values from N machines.
 * It continuously took requests from N machines and flush it to reducers at regular intervals.
 *
 * Minesweeper -> code
 *
 */
public interface RoutingService {

    public void updateCounters(final Map<Integer, Long> counterMap);
}
