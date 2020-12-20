package com.joel;

import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FlightRecorderReader {

    public static void main(String[] args) throws IOException {
        Path p = Paths.get("recording.jfr");
        for (RecordedEvent e : RecordingFile.readAllEvents(p)) {
            System.out.println(e.getStartTime() + " : " + e.getValue("message"));
        }
    }

}
