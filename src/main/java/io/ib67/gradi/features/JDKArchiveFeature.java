package io.ib67.gradi.features;

import io.ib67.gradi.GradiFeature;
import io.ib67.gradi.ProcessResult;
import io.ib67.gradi.data.GradiContext;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class JDKArchiveFeature implements GradiFeature {
    private static final Pattern PATTERN = Pattern.compile("\\.gradle/jdks/.*.(gz|zip|tar|xz)$");

    @Override
    public ProcessResult process(Path target, BasicFileAttributes attributes, GradiContext context) {
        if (attributes.isRegularFile()) {
            if (PATTERN.matcher(target.toAbsolutePath().toString()).find()) {
                if (target.getFileName().endsWith("src.zip")) {
                    return ProcessResult.KEEP; // SKIP.
                }
                System.out.println("[!!] JDK Archive Related: " + target);
                return ProcessResult.REMOVED;
            }
        }
        return ProcessResult.KEEP;
    }
}
