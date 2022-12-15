package io.ib67.gradi.features;

import io.ib67.gradi.GradiFeature;
import io.ib67.gradi.ProcessResult;
import io.ib67.gradi.data.GradiContext;
import io.ib67.gradi.util.PathUtil;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

import static io.ib67.gradi.ProcessResult.KEEP;

public class GlobalCacheFeature implements GradiFeature {
    private static Pattern PATTERN = Pattern.compile("\\.gradle/caches/jars-\\d/.*.jar");

    @Override
    public ProcessResult process(Path target, BasicFileAttributes attributes, GradiContext context) {
        if (attributes.isRegularFile()) {
            if (PATTERN.matcher(PathUtil.toUnixPath(target.toAbsolutePath().toString())).find()) {
                // parent dir.
                var parent = target.getParent();
                if (System.currentTimeMillis() - parent.toFile().lastModified() > context.configuration().gradleGlobalCachePeriod()) {
                    System.out.println("[!!] Expired Gradle Cache(" + target.toFile().length() / 1024 / 1024 + "M): " + target);
                }
                return ProcessResult.REMOVED;
            }
        }
        return KEEP;
    }
}
