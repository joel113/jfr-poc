# JfrUnit - Proof of Concept

This project investigates the JfrUnit framework for measuring and asserting
performance-impacting metrics with the JDK Flight Recorder. I disovered the
project when I read a Twitter post of Gunnar Morling: 

https://twitter.com/gunnarmorling/status/1339292020607754240

In the following blog post, Gunnar introduces JfrUnit as tool for Continuous 
Performance Regression Testing:

https://www.morling.dev/blog/towards-continuous-performance-regression-testing/

The blog post of Gunnar introduces the following Git Hub project.

https://github.com/gunnarmorling/jfrunit

The JfrUnit framework is build on top of the JDK Flight Recorder:

https://openjdk.java.net/jeps/328

Hence, I did not only investigate the JfrUnit framework, but also the Java Flight 
Recorder itself.

## Java Flight Recorder

The Java Flight Recorder is a Java event profiler provided with the Java JDK.
The Java Mission Control application is a tool to anlyse flight recordings. 
The flight recordings help to analyse the Java application and the Java Virtual Machine.

Please check the following link to find more information about the Java Mission Control: https://www.oracle.com/java/technologies/jdk-mission-control.html

The following article gives a good overview of the Java Flight Recorder:

https://jaxenter.de/java-flight-recorder-57537

From Java 11 on, JFR was contributed to the OpenJDK and requires no commercial license anymore:

https://blogs.oracle.com/javamagazine/java-flight-recorder-and-jfr-event-streaming-in-java-14

### JMX Console & MBean Server

The JMX console is another profiling application which connects to a MBean Server to retrieve the metrics from
the MBean server.

A MBean Server is part of the Java Management Extension (JMX) framework, where a MBean Server
is the core component to provide profiling data to the JMX console.

Please check https://www.baeldung.com/java-management-extensions for more information about
the Java Management Extension as well as MBean programming.

### Java Mission Control

The Java Mission Control is an application which allows to connect to a MBean server as well as
analyzing Java Flight Recorder files:

https://blog.avenuecode.com/java-mission-control-and-java-flight-recorder

## Flight Recorder Usage

The JDK Flight Recorder has to be activated using command line flags:

    java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder

From Java 8 update 40 it is also possible to activate the Java Flight Recorder
on the fly: http://www.oracle.com/us/corporate/press/2436684.

The following command does start the flight recorder and records flight recorder
events for a fixed time:

     sh -c 'cd target/classes &&  /opt/jdk-15.0.1/bin/java com.joel.JavaFlightRecorder -XX:StartFlightRecording=delay=20s,duration=60s,name=Test,filename=recording.jfr,settings=profile'

When trying the commands, please check the following GitHub repository for 
a nice collection of Java applications which deserve some profiling:

https://github.com/chrishantha/sample-java-programs/releases/tag/v0.0.1

Please be aware that some of the flags which are used on the linked pages are
actually not needed anymore:

https://bugs.openjdk.java.net/browse/JDK-8225312

Hence, the arguments

     -XX:+UnlockComvercialFeatures
     -XX:+FlightRecorder

are not needed anymore. E.g., the following command is sufficient to capture the 
Flight Recorder of the high cpu application of chrishanthas project:

     /opt/jdk-15.0.1/bin/java -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:StartFlightRecording=delay=20s,duration=60s,name=Test,filename=recording.jfr,settings=profile -jar target/highcpu.jar

With the "jcmd" tool it is even possible to send diagnostic command requests to the
JVM.

Execution the

     jcmd

command gives an process overview:

    38803 org.jetbrains.jps.cmdline.Launcher /opt/idea-IC-191.7141.44/lib/netty-buffer-4.1.52.Final.jar:/opt/idea-IC-191.7141.44/plugins/java/lib/aether-dependency-resolver.jar:/opt/idea-IC-191.7141.44/lib/plexus-interpolation-1.25.jar:/opt/idea-IC-191.7141.44/lib/forms-1.1-preview.jar:/opt/idea-IC-191.7141.44/lib/jps-model.jar:/opt/idea-IC-191.7141.44/plugins/java/lib/jps-builders.jar:/opt/idea-IC-191.7141.44/lib/netty-resolver-4.1.52.Final.jar:/opt/idea-IC-191.7141.44/lib/jna-platform.jar:/opt/idea-IC-191.7141.44/lib/maven-model-builder-3.6.1.jar:/opt/idea-IC-191.7141.44/plugins/java/lib/maven-resolver-transport-file-1.3.3.jar:/opt/idea-IC-191.7141.44/lib/asm-all-9.0.jar:/opt/idea-IC-191.7141.44/lib/httpclient-4.5.12.jar:/opt/idea-IC-191.7141.44/lib/commons-logging-1.2.jar:/opt/idea-IC-191.7141.44/lib/netty-codec-4.1.52.Final.jar:/opt/idea-IC-191.7141.44/lib/jdom.jar:/opt/idea-IC-191.7141.44/lib/netty-transport-4.1.52.Final.jar:/opt/idea-IC-191.7141.44/lib/maven-model-3.6.1.jar:/opt/idea-IC-191.7141.44/lib/commons-la
    339976 jdk.jcmd/sun.tools.jcmd.JCmd
    339914 target/highcpu.jar
    314173 com.intellij.idea.Main

Executing the 

    jcmd highcpu help 

command gives an overview of the jcmd possibilities:

    joel@...:/.../jdk-15.0.1/bin$ ./jcmd highcpu help
    39914:
    The following commands are available:
    Compiler.CodeHeap_Analytics
    Compiler.codecache
    Compiler.codelist
    Compiler.directives_add
    Compiler.directives_clear
    Compiler.directives_print
    Compiler.directives_remove
    Compiler.queue
    GC.class_histogram
    GC.finalizer_info
    GC.heap_dump
    GC.heap_info
    GC.run
    GC.run_finalization
    JFR.check
    JFR.configure
    JFR.dump
    JFR.start
    JFR.stop
    JVMTI.agent_load
    JVMTI.data_dump
    ManagementAgent.start
    ManagementAgent.start_local
    ManagementAgent.status
    ManagementAgent.stop
    Thread.print
    VM.class_hierarchy
    VM.classloader_stats
    VM.classloaders
    VM.command_line
    VM.dynlibs
    VM.events
    VM.flags
    VM.info
    VM.log
    VM.metaspace
    VM.native_memory
    VM.print_touched_methods
    VM.set_flag
    VM.stringtable
    VM.symboltable
    VM.system_properties
    VM.systemdictionary
    VM.uptime
    VM.version
    help

The command 

    jfr print recording.jfr

allows to print the Java Flight Recorder events to the command line:

    /.../jdk-15.0.1/bin/jfr print recording.jfr

The following page gives an introduction into the usage of the Java Flight
Recorder:

https://medium.com/@chrishantha/using-java-flight-recorder-2367c01deacf

The following page summarizes the commands to use the Java Flight Recorder into
a cheat sheet:

https://medium.com/@chrishantha/java-flight-recorder-cheat-sheet-98f5143f5f88

### Further Metric Projects

Besides the Java Flight Recorder there are other projects which allow to expose
Java metrics, e.g.:

https://github.com/ultrabrew/metrics

https://metrics.dropwizard.io/

https://micrometer.io/

https://www.reddit.com/r/java/comments/92be5h/java_metrics_alternatives/

## Flight Recorder in IntelliJ

https://blog.jetbrains.com/idea/2020/03/profiling-tools-and-intellij-idea-ultimate/

## JfrUnit - POC Usage

export JAVA_HOME=/.../jdk-15.0.1/

/.../jdk-15.0.1/bin/java com.joel.JavaFlightRecorder -XX:StartFlightRecording

sh -c 'cd target/classes &&  /.../jdk-15.0.1/bin/java com.joel.JavaFlightRecorder -XX:StartFlightRecording'

https://stackoverflow.com/questions/24720665/cant-resolve-jdk-internal-package

## Related Projects

https://github.com/quick-perf

## Project Requirements

Java 15
