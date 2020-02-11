package src.main.java.spawn;

import src.main.java.Controller;

import java.util.*;

class TimelineController implements Runnable {

    private final Map<Integer, TimeStampEvent> timelineEvents;
    private final Queue<Integer> timelineQueue;
    private long startTime;

    double speedMultiplier = 1;

    public TimelineController() {
        timelineEvents = TimelineUtil.readTimelines();

        startTime = System.currentTimeMillis();

        List<Integer> timeStamps = new ArrayList<>(timelineEvents.keySet());
        Collections.sort(timeStamps);
        timelineQueue = new LinkedList<>(timeStamps);
    }

    public void run() {


        if (timelineQueue.size() == 0)
            return;

        long currentTime = System.currentTimeMillis();

        int nextTimeStamp = timelineQueue.remove();

        while (true) {
            long secondsPassed = (currentTime - startTime) / 1000;

            if (secondsPassed > nextTimeStamp) {
                System.out.print(",");
                Controller.updateSpawnProbabilities(timelineEvents.get(nextTimeStamp));

                if (timelineQueue.size() == 0) {
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