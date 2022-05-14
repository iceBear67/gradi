package io.ib67.gradi.features;

import io.ib67.gradi.GradiFeature;
import io.ib67.gradi.ProcessResult;
import io.ib67.gradi.data.GradiContext;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class UnusedDistFeature implements GradiFeature {
    private static final Pattern PART = Pattern.compile(".*.part$");
    private static final Pattern DIST = Pattern.compile("dists/gradle-(.*)-(bin$|all$)");

    @Override
    public ProcessResult process(Path target, BasicFileAttributes attributes, GradiContext context) {
        if (attributes.isRegularFile()) {
            if (PART.matcher(target.toAbsolutePath().toString()).find()) {
                return ProcessResult.REMOVED;
            }
        } else if (attributes.isDirectory()) {
            var matcher = DIST.matcher(target.toAbsolutePath().toString());
            if (matcher.find()) {
                var gradleVersion = matcher.group(1);
                var daemonPath = context.configuration().gradleHome().resolve("daemon").resolve(gradleVersion);
                if (!context.usingGradleVersions().contains(gradleVersion)) {
                    System.out.println("[!!] Unused Daemon: " + target.toAbsolutePath());
                    context.pathsToBeCleaned().add(daemonPath);
                    return ProcessResult.REMOVED;
                } else {
                    // check for last modified time.
                    if (System.currentTimeMillis() - daemonPath.resolve("registry.bin").toFile().lastModified() >= context.configuration().removeGradlePeriod()) {
                        // do remove....
                        System.out.println("[!!] Expired Daemon: " + target.toAbsolutePath());
                        context.pathsToBeCleaned().add(daemonPath);
                        return ProcessResult.REMOVED;
                    }
                }
            }
        }
        return ProcessResult.KEEP;
    }
}
