package io.ib67.gradi;

import io.ib67.gradi.data.GradiContext;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface GradiFeature {
    ProcessResult process(Path target, BasicFileAttributes attributes, GradiContext context);
}
