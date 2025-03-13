package kp;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Writer for the data set.
 */
public class DatasetWriter {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final String SRC_MAIN_DIR_FRAG = "%1$ssrc%1$smain%1$s".formatted(File.separator);
    private static final String SRC_TEST_DIR_FRAG = "%1$ssrc%1$stest%1$s".formatted(File.separator);
    private static final String BATCH_DIR_FRAG = "%1$s0_batch%1$s".formatted(File.separator);
    private static final String DOCS_DIR_FRAG = "%1$sdocs%1$s".formatted(File.separator);

    private static final Function<Path, String> PATH_FUN = path -> Optional.ofNullable(path)
            .map(Path::toString).orElse("");
    private static final Function<Path, String> FILE_NAME_FUN = path -> Optional.ofNullable(path)
            .map(Path::getFileName).map(Path::toString).orElse("");
    private static final Function<Path, String> FILE_EXT_FUN = path -> Optional.of(FILE_NAME_FUN.apply(path))
            .filter(name -> name.lastIndexOf('.') > 0)
            .map(name -> name.substring(name.lastIndexOf('.') + 1)).orElse("");

    /**
     * Writes the data set.
     *
     * @param datasetMap the map with the data set
     */
    static void writeDataset(Map<String, Map<Path, Map<Integer, Set<String>>>> datasetMap) {

        final JsonFactory jsonFactory = new JsonFactory();
        final StringWriter stringWriter = new StringWriter();
        try {
            try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter)) {
                jsonGenerator.useDefaultPrettyPrinter();
                jsonGenerator.writeStartArray();
                writeDataArray(datasetMap.entrySet(), jsonGenerator);
                jsonGenerator.writeEndArray();
            }
            Files.writeString(Path.of(Constants.DATA_SET_FILE), stringWriter.toString(), Charset.defaultCharset());
        } catch (IOException e) {
            logger.error("writeDataset(): IOException[{}]", e.getMessage());
        }
        if (logger.isInfoEnabled()) {
            logger.info("writeDataset(): data set file[{}]", Constants.DATA_SET_FILE);
        }
    }

    /**
     * Writes the data array.
     *
     * @param datasetEntrySet the set of entries from the data set map
     * @param jsonGenerator   the {@link JsonGenerator}
     */
    static private void writeDataArray(Set<Map.Entry<String, Map<Path, Map<Integer, Set<String>>>>> datasetEntrySet,
                                       JsonGenerator jsonGenerator) {

        final AtomicInteger atomic = new AtomicInteger();
        datasetEntrySet.forEach(
                projectEntry -> projectEntry.getValue().forEach(
                        (key1, value1) -> value1.forEach(
                                (key2, value2) -> value2.forEach(
                                        keyword -> writeDataElement(jsonGenerator, atomic.incrementAndGet(),
                                                projectEntry.getKey(), key1, key2, keyword)
                                ))));
    }

    /**
     * Writes the data element.
     *
     * @param jsonGenerator the {@link JsonGenerator}
     * @param id            the id
     * @param project       the project name
     * @param path          the {@link Path}
     * @param lineNumber    the line number
     * @param keyword       the keyword
     */
    static private void writeDataElement(JsonGenerator jsonGenerator, int id, String project,
                                         Path path, Integer lineNumber, String keyword) {

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", id);
            jsonGenerator.writeStringField("keyword", keyword.toLowerCase());
            jsonGenerator.writeStringField("project", project);
            jsonGenerator.writeStringField("category", getCategory(path.toString()));
            jsonGenerator.writeStringField("path", PATH_FUN.apply(path));
            jsonGenerator.writeStringField("file-name", FILE_NAME_FUN.apply(path));
            jsonGenerator.writeStringField("file-extension", FILE_EXT_FUN.apply(path));
            jsonGenerator.writeNumberField("line-number", lineNumber);
            jsonGenerator.writeEndObject();
        } catch (IOException e) {
            logger.error("writeDataElement(): IOException[{}]", e.getMessage());
        }
    }

    /**
     * Gets the category.
     *
     * @param pathString the path string
     * @return the category
     */
    static private String getCategory(String pathString) {

        if (pathString.contains(SRC_MAIN_DIR_FRAG)) {
            return "main-source";
        } else if (pathString.contains(SRC_TEST_DIR_FRAG)) {
            return "tests";
        } else if (pathString.contains(BATCH_DIR_FRAG)) {
            return "batch";
        } else if (pathString.contains(DOCS_DIR_FRAG)) {
            return "documentation";
        } else {
            return "";
        }
    }

}
