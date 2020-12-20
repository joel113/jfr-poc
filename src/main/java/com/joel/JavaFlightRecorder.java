package com.joel;

import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

import java.io.IOException;

/**
 * Defines a event {@https://docs.oracle.com/en/java/javase/14/docs/api/jdk.jfr/jdk/jfr/Event.html} in order
 * to commit the event to the Java Flight Recorder.
 */
public class JavaFlightRecorder {

    @Label("Hello World")
    @Description("Helps the programmer getting started")
    static class HelloWorld extends Event {
        @Label("Message")
        String message;
    }

    /**
     * After an event is allocated, it can be written to the Flight Recorder system using commit.
     *
     * @param args Arguments
     * @throws IOException
     */
    public static void main(String... args) throws IOException {
        HelloWorld event = new HelloWorld();
        event.message = "hello, world!";
        event.commit();
        System.out.println("hello, world!");
    }

}
