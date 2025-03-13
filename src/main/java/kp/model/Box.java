package kp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Box record.
 *
 * @param id            the id
 * @param keyword       the keyword
 * @param project       the project
 * @param category      the category
 * @param path          the path
 * @param fileName      the name of the file in path
 * @param fileExtension the extension of the file in path
 * @param lineNumber    the line number of the code
 */
public record Box(
        String id,
        String keyword,
        String project,
        String category,
        String path,
        @JsonProperty("file-name") String fileName,
        @JsonProperty("file-extension") String fileExtension,
        @JsonProperty("line-number") int lineNumber) {
}
