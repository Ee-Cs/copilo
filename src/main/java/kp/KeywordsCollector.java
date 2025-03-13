package kp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collects the keywords from projects.
 */
public class KeywordsCollector {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final Set<String> foundKeywordsSet = new HashSet<>();

    /**
     * Collects keywords.
     *
     * @return the data set map
     */
    static Map<String, Map<Path, Map<Integer, Set<String>>>> collectKeywords() {

        foundKeywordsSet.clear();
        final Map<String, Map<Path, Map<Integer, Set<String>>>> datasetMap = new TreeMap<>();
        Constants.DIRECTORIES_LIST.forEach(project -> {
            datasetMap.putIfAbsent(project, new TreeMap<>());
            visitProject(datasetMap.get(project), project);
        });
        logger.info("collectKeywords(): found all keywords[{}]",
                foundKeywordsSet.containsAll(Constants.KEYWORDS_LIST));
        return datasetMap;
    }

    /**
     * Visits the project directory.
     *
     * @param projectMap the project map
     * @param project    the project
     */
    private static void visitProject(Map<Path, Map<Integer, Set<String>>> projectMap, String project) {

        final Path projectPath = FileSystems.getDefault().getPath("..", project).toAbsolutePath().normalize();
        final Deque<Path> stack = new ArrayDeque<>();
        stack.push(projectPath);
        try {
            while (!stack.isEmpty()) {
                final DirectoryStream<Path> stream = Files.newDirectoryStream(stack.pop());
                try (stream) {
                    stream.forEach(path -> visitDirectory(projectMap, stack, path));
                }
            }
        } catch (IOException e) {
            logger.error("visitProject(): IOException[{}]", e.getMessage());
            System.exit(1);
        }
        logger.info("visitProject(): path[{}]", projectPath);
    }

    /**
     * Visits the directory.
     *
     * @param projectMap the map with projects
     * @param stack      the stack with {@link Path}s
     * @param path       the {@link Path}
     */
    private static void visitDirectory(Map<Path, Map<Integer, Set<String>>> projectMap, Deque<Path> stack, Path path) {

        if (Files.isDirectory(path)) {
            final boolean directoryExcluded = Optional.of(path).map(Path::getFileName).map(Path::toString)
                    .filter(Predicate.not(Constants.EXCLUDED_DIRECTORIES::contains)).isEmpty();
            if (directoryExcluded) {
                if (logger.isDebugEnabled()) {
                    logger.debug("visitDirectory(): excluded directory[{}]", path);
                }
                return;
            }
            stack.push(path);
            return;
        }
        if (!validateFile(path)) {
            return;
        }
        projectMap.putIfAbsent(path, new TreeMap<>());
        readFile(projectMap.get(path), path);
    }

    /**
     * Validates the file.
     *
     * @param path the {@link Path}
     */
    private static boolean validateFile(Path path) {

        final Optional<String> fileNameOpt = Optional.ofNullable(path).map(Path::toFile).filter(File::canRead)
                .map(File::getName).filter(name -> name.contains("."));
        if (fileNameOpt.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("validateFile(): file unreadable or without extension, path[{}]", path);
            }
            return false;
        }
        final String fileName = fileNameOpt.get();
        if (Constants.INCLUDED_FILES.contains(fileName)) {
            return true;
        }
        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (Constants.INCLUDED_EXTENSIONS.contains(extension)) {
            return true;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("validateFile(): excluded file with extension[{}], path[{}]", extension, path);
        }
        return false;
    }

    /**
     * Reads the file.
     *
     * @param fileMap the file map
     * @param path    the {@link Path}
     */
    private static void readFile(Map<Integer, Set<String>> fileMap, Path path) {

        final AtomicInteger atomic = new AtomicInteger();
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            final Stream<String> linesStream = bufferedReader.lines();
            linesStream.sequential().forEachOrdered(line -> {
                final int lineNumber = atomic.incrementAndGet();
                final Set<String> keywordsSet = Stream.of(Constants.DELIMITER.split(line))
                        .filter(Predicate.not(String::isBlank)).map(String::toUpperCase)
                        .filter(Constants.KEYWORDS_LIST::contains).collect(Collectors.toSet());
                if (keywordsSet.isEmpty()) {
                    return;
                }
                foundKeywordsSet.addAll(keywordsSet);
                fileMap.put(lineNumber, keywordsSet);
                if (logger.isDebugEnabled()) {
                    logger.debug("readFile(): path[{}]\n\tline[{}], keywords{}", path, lineNumber, keywordsSet);
                }
            });
        } catch (IOException ex) {
            logger.error("readFile(): exception[{}], path[{}]", ex.getMessage(), path);
            System.exit(1);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("readFile(): processed path[{}]", path);
        }
    }

}
