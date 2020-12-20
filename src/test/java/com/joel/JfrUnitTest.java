package com.joel;

import dev.morling.jfrunit.*;
import org.junit.jupiter.api.Test;

import static dev.morling.jfrunit.ExpectedEvent.event;
import static dev.morling.jfrunit.JfrEventsAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

@JfrEventTest
public class JfrUnitTest {

    public JfrEvents jfrEvents = new JfrEvents();

    @Test
    @EnableEvent("jdk.GarbageCollection")
    @EnableEvent("jdk.ThreadSleep")
    public void shouldHaveGcAndSleepEvents() throws Exception {
        System.gc();
        Thread.sleep(1000);

        jfrEvents.awaitEvents();

        assertThat(jfrEvents).contains(event("jdk.GarbageCollection"));
        assertThat(jfrEvents).contains(event("jdk.GarbageCollection").with("cause", "System.gc()"));
        assertThat(jfrEvents).contains(event("jdk.ThreadSleep").with("time", Duration.ofSeconds(1)));
        assertThat(jfrEvents.filter(event("jdk.GarbageCollection"))).hasSize(1);
    }

    @Test
    @EnableConfiguration("profile")
    public void shouldHaveGcAndSleepEventsWithDefaultConfiguration() throws Exception {
        System.gc();
        Thread.sleep(1000);

        jfrEvents.awaitEvents();

        assertThat(jfrEvents).contains(event("jdk.GarbageCollection"));
        assertThat(jfrEvents).contains(event("jdk.GarbageCollection").with("cause", "System.gc()"));
        assertThat(jfrEvents).contains(event("jdk.ThreadSleep").with("time", Duration.ofSeconds(1)));
        assertThat(jfrEvents.filter(event("jdk.GarbageCollection").with("cause", "System.gc()"))).hasSize(1);

        long allocated = jfrEvents.filter(event("jdk.ObjectAllocationInNewTLAB"))
                .mapToLong(e -> e.getLong("tlabSize"))
                .sum();

        assertThat(allocated).isGreaterThan(0);
    }

}
