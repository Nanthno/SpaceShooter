package src.main.java.spawn;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineUtil {

    static final File timelineDirectory = new File("src/main/resources/spawn/timeline");


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
                    if(timeline.containsKey(time))
                        event = timeline.get(time);
                    else
                        event = new TimeStampEvent();
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

            }
            if(time != -1) {
                timeline.put(time, event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeline;
    }

}