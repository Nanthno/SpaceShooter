package src.main.java.spawn;

import src.main.java.Controller;

import java.util.*;

public class TimelineController implements Runnable {

    Map<Integer, TimeStampEvent> timelineEvents;
    Queue<Integer> timelineQueue;
    long startTime;

    public TimelineController() {
        timelineEvents = TimelineUtil.readTimelines();

        startTime = System.currentTimeMillis();

        List<Integer> timeStamps = new ArrayList<Integer>();
        timeStamps.addAll(timelineEvents.keySet());
        Collections.sort(timeStamps);
        timelineQueue = new LinkedList<Integer>(timeStamps);
    }

    public void run() {

        long currentTime = System.currentTimeMillis();

        while (timelineQueue.size() > 0) {
            int nextTimeStamp = timelineQueue.remove();
            long secondsPassed = (currentTime - startTime) / 1000;

            if (secondsPassed > nextTimeStamp) {
                Controller.updateSpawnProbabilities(timelineEvents.get(nextTimeStamp));
            }
        }
    }


}