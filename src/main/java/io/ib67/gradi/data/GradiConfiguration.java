package io.ib67.gradi.data;

import io.ib67.gradi.GradiFeature;

import java.nio.file.Path;
import java.util.List;

public record GradiConfiguration(
        Path gradleHome,
        Path projectsHome,
        List<GradiFeature> enabledFeatures,

        long removeGradlePeriod,
        long gradleGlobalCachePeriod
) {
}
