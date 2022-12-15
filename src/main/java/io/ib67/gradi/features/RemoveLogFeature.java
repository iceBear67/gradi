package io.ib67.gradi.features;

import io.ib67.gradi.GradiFeature;
import io.ib67.gradi.ProcessResult;
import io.ib67.gradi.data.GradiContext;
import io.ib67.gradi.util.PathUtil;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class RemoveLogFeature implements GradiFeature {
    private static final Pattern DAEMON_LOG_PATTERN = Pattern.compile("daemon/.*/daemon-\\d+.*.log$");

    @Override
    public ProcessResult process(Path target, BasicFileAttributes attributes, GradiContext context) {
        if (attributes.isRegularFile() && DAEMON_LOG_PATTERN.matcher(PathUtil.toUnixPath(target.toAbsolutePath().toString())).find()) {
            System.out.println("[--] Daemon Log(" + target.toFile().length() / 1024 + "k): " + target.toAbsolutePath());
            return ProcessResult.REMOVED;
        }
        return ProcessResult.KEEP;
    }
}
