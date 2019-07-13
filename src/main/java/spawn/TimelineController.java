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


        if(timelineQueue.size() == 0)
            return;

        long currentTime = System.currentTimeMillis();

        int nextTimeStamp = timelineQueue.remove();

        while (true) {
            long secondsPassed = (currentTime - startTime) / 1000;

            if (secondsPassed > nextTimeStamp) {
                System.out.print(",");
                Controller.updateSpawnProbabilities(timelineEvents.get(nextTimeStamp));

                if(timelineQueue.size() == 0) {
                    return;
                }

                nextTimeStamp = timelineQueue.remove();
            }
        }
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}