package io.ib67.gradi;

import io.ib67.gradi.data.GradiConfiguration;
import io.ib67.gradi.features.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * BootStrap class, which initialize a CLI
 */
public final class GradiBoot {
    private static final Map<String, GradiFeature> FEATURES;

    static {
        FEATURES = Map.of(
                "dist:backup", new DistBackupFeature(),
                "dist:unused", new UnusedDistFeature(),
                "log:remove", new RemoveLogFeature(),
                "jdk:archives", new JDKArchiveFeature(),
                "cache:buildscript", new BuildScriptCacheFeature(),
                "cache:global", new GlobalCacheFeature()
        );
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: gradi <Directory of Projects> <day to remove unused gradle> <day to remove global cache>");
            System.out.println("E.g ~/IdeaProjects 30 14");
            return;
        }
        new Gradi(
                new GradiConfiguration(
                        Path.of(System.getProperty("user.home")).resolve(".gradle"),
                        Path.of(args[0]),
                        List.of(new DistBackupFeature(),
                                new UnusedDistFeature(),
                                new RemoveLogFeature(),
                                new JDKArchiveFeature(),
                                new BuildScriptCacheFeature(),
                                new GlobalCacheFeature()
                        ),
                        Integer.parseInt(args[1]) * 24 * 60 * 60 * 1000L,
                        Integer.parseInt(args[2]) * 24 * 60 * 60 * 1000L
                )
        ).clean();
    }
}
