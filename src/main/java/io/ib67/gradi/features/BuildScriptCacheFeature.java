package io.ib67.gradi.features;

import io.ib67.gradi.GradiFeature;
import io.ib67.gradi.ProcessResult;
import io.ib67.gradi.data.GradiContext;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class BuildScriptCacheFeature implements GradiFeature {
    private static final Pattern PATTERN = Pattern.compile("\\.gradle/caches/.*/scripts$");

    @Override
    @SneakyThrows
    public ProcessResult process(Path target, BasicFileAttributes attributes, GradiContext context) {
        if (attributes.isDirectory()) {
            if (PATTERN.matcher(target.toAbsolutePath().toString()).find()) {
                System.out.println("[!!] Build Script Caches: " + target.toAbsolutePath());
                return ProcessResult.REMOVED;
            }
        }
        return ProcessResult.KEEP;
    }
}
