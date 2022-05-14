package io.ib67.gradi.data;

import java.nio.file.Path;
import java.util.Set;

public record GradiContext(
        Set<String> usingGradleVersions,
        GradiConfiguration configuration,
        Set<Path> pathsToBeCleaned
) {

}
