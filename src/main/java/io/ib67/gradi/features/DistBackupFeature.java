package io.ib67.gradi.features;

import io.ib67.gradi.GradiFeature;
import io.ib67.gradi.ProcessResult;
import io.ib67.gradi.data.GradiContext;
import io.ib67.gradi.util.PathUtil;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class DistBackupFeature implements GradiFeature {
    private static final Pattern DIST = Pattern.compile("gradle-.*?-bin/.*/gradle.*bin.zip?$");

    @Override
    public ProcessResult process(Path target, BasicFileAttributes attributes, GradiContext context) {
        if (attributes.isRegularFile()) {
            if (DIST.matcher(PathUtil.toUnixPath(target.toAbsolutePath().toString())).find()) {
                System.out.println("[!!] Dist Backup: " + target.toAbsolutePath());
                return ProcessResult.REMOVED;
            }
        }
        return ProcessResult.KEEP;
    }
}
