package com.gatling;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.util.Objects.requireNonNull;

public class IDEPathHelper {

    static final Path mavenSourcesDirectory;
    static final Path mavenResourcesDirectory;
    static final Path mavenBinariesDirectory;
    static final Path resultsDirectory;
    static final Path recorderConfigFile;

    static {
        try {
            Path projectRootDir = Paths.get(requireNonNull(IDEPathHelper.class.getResource(
                "resources/gatling.conf"), "Couldn't locate gatling.conf").toURI()).getParent().getParent().getParent()
                .getParent().getParent().getParent();
            Path mavenTargetDirectory = projectRootDir.resolve("target");
            Path mavenSrcTestDirectory = projectRootDir.resolve("test").resolve("com");

            mavenSourcesDirectory = mavenSrcTestDirectory.resolve("gatling");
            mavenResourcesDirectory = mavenSrcTestDirectory.resolve("");
            mavenBinariesDirectory = mavenTargetDirectory.resolve("test-classes");
            resultsDirectory = mavenTargetDirectory.resolve("gatling");
            recorderConfigFile = mavenResourcesDirectory.resolve("/com/gatling/resources/recorder.conf");
        } catch (URISyntaxException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
