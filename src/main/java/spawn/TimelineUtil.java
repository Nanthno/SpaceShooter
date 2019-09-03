package src.main.java.spawn;

import src.main.java.Globals;
import src.main.java.ResourceFileType;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.util.*;

public class TimelineUtil {

    static final File timelineDirectory = new File(Globals.getResourceFile(ResourceFileType.TIMELINE));


    public static Map<Integer, TimeStampEvent> readTimelines() {
        Map<Integer, TimeStampEvent> timeline = new HashMap<>();

        File[] timelineFiles = timelineDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".timeline");
            }
        });

        for (File file : timelineFiles) {
            timeline = unpackFile(file, timeline);
        }

        return timeline;
    }

    public static Queue<TimeStampEvent> readTimelinesToQueue() {
        Map<Integer, TimeStampEvent> timelineMap = readTimelines();

        List<Integer> timePoints = new ArrayList<Integer>();
        timePoints.addAll(timelineMap.keySet());
        Collections.sort(timePoints);

        Queue<TimeStampEvent> events = new LinkedList<>();

        for (int i : timePoints) {
            events.add(timelineMap.get(i));
        }

        return events;
    }

    private static Map<Integer, TimeStampEvent> unpackFile(File file, Map<Integer, TimeStampEvent> timeline) {

        try {
            List<String> documentList = Files.readAllLines(file.toPath());
            String[] document = documentList.toArray(new String[documentList.size()]);


            TimeStampEvent event = null;
            int time = -1;
            for (int i = 0; i < document.length; i++) {
                if (document[i].startsWith("time")) {
                    if (event != null && time != -1 && !timeline.containsKey(time)) {
                        timeline.put(time, event);
                    }
                    time = Integer.parseInt(document[i].split(" ")[1]);
                    if (timeline.containsKey(time))
                        event = timeline.get(time);
                    else
                        event = new TimeStampEvent(time);
                    continue;
                }

                if (document[i].startsWith("set")) {
                    event.addSet(document[i]);
                    continue;
                }
                if (document[i].startsWith("spawn")) {
                    event.addSpawn(document[i]);
                    continue;
                }
                if(document[i].startsWith("speed")) {
                    try {
                        double speed = Double.parseDouble(document[i].substring(5));
                        event.setSpeed(speed);
                    } catch(Exception e) {
                        System.out.println("Could not parse speed in timeline at line " + i);
                        e.printStackTrace();
                    }
                }

            }
            if (time != -1) {
                timeline.put(time, event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeline;
    }

}
