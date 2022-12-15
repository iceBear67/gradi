package io.ib67.gradi;

import io.ib67.gradi.data.GradiConfiguration;
import io.ib67.gradi.data.GradiContext;
import io.ib67.gradi.util.PathUtil;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public final class Gradi {
    private static final Pattern GRADLE_VERSION_PATTERN = Pattern.compile("\\.gradle/(.*)/gc.properties$");
    @Getter
    private final GradiConfiguration configuration;
    private final GradiContext context;

    public Gradi(GradiConfiguration configuration) {
        Objects.requireNonNull(configuration);
        Objects.requireNonNull(configuration.gradleHome());
        Objects.requireNonNull(configuration.enabledFeatures());
        this.configuration = configuration;
        context = new GradiContext(
                getUsingGradles(),
                configuration,
                new LinkedHashSet<>(512)
        );
    }

    private Set<String> getUsingGradles() {
        var projHome = configuration.projectsHome();
        var results = new HashSet<String>();
        try {
            Files.walkFileTree(projHome, new GradiFileVisitor((path, attributes) -> {
                if (attributes.isRegularFile() && path.getFileName().toString().equals("gc.properties")) {
                    // read ti.
                    var matcher = GRADLE_VERSION_PATTERN.matcher(PathUtil.toUnixPath(path.toString()));
                    if (!matcher.find()) {
                        return FileVisitResult.CONTINUE;
                    }
                    results.add(matcher.group(1));
                }
                return FileVisitResult.CONTINUE;
            }, t -> {
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    @SneakyThrows
    public void clean() {
        try {
            Files.walkFileTree(configuration.gradleHome(), new GradiFileVisitor(this::accept, this::cleanEmptyDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[!!!] Deleting " + context.pathsToBeCleaned().size() + " directories");
        // start to delete.
        for (Path path : context.pathsToBeCleaned()) {
            if (Files.exists(path)) {
                deleteDirectoryRecursion(path);
            }
        }
        System.out.println("Done!");
    }

    void deleteDirectoryRecursion(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursion(entry);
                }
            }
        }
        Files.delete(path);
    }

    @SneakyThrows
    private void cleanEmptyDir(Path path) {
        if (Objects.requireNonNull(path.toFile().listFiles()).length == 0) {
            context.pathsToBeCleaned().add(path);
        }
    }

    private FileVisitResult accept(Path path, BasicFileAttributes attrs) {
        //    System.out.println("[..] Processing Path: "+path);
        for (GradiFeature enabledFeature : configuration.enabledFeatures()) {
            var result = enabledFeature.process(path, attrs, context);
            if (result == ProcessResult.REMOVED) {
                context.pathsToBeCleaned().add(path);
                if (attrs.isDirectory()) {
                    return FileVisitResult.SKIP_SUBTREE; // directory -- skip entries
                } else {
                    return FileVisitResult.CONTINUE; // process next file
                }
            }
        }
        return FileVisitResult.CONTINUE;
    }

}
