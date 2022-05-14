package io.ib67.gradi;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class GradiFileVisitor implements FileVisitor<Path> {
    private final BiFunction<Path, BasicFileAttributes, FileVisitResult> consumer;
    private final Consumer<Path> cleanUp;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);
        return consumer.apply(dir, attrs);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(file);
        Objects.requireNonNull(attrs);
        return consumer.apply(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        Objects.requireNonNull(file);
        throw exc;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Objects.requireNonNull(dir);
        if (exc != null)
            throw exc;
        cleanUp.accept(dir);
        return FileVisitResult.CONTINUE;
    }
}
