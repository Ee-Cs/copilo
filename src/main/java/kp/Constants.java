package kp;

import java.util.List;
import java.util.regex.Pattern;

/**
 * The constants.
 */
public final class Constants {
    /**
     * The data set file path.
     */
    static final String DATA_SET_FILE = "solr-requests\\dataset.json";
    /**
     * The directories of the projects with keywords.
     * <p>
     * The projects 'Study15' (with Solr) and 'Study16' (with Elasticsearch) were intentionally not included.
     * </p>
     */
    static final List<String> DIRECTORIES_LIST = List.of("Miscellany",
            "Study01", "Study02", "Study03", "Study04", "Study05",
            "Study06", "Study07", "Study08", "Study09", "Study10",
            "Study11", "Study12", "Study13", "Study14",
            "Study17", "Study18", "Study19", "Study20");
    /**
     * The excluded directories.
     */
    static final List<String> EXCLUDED_DIRECTORIES = List.of("target", ".metadata", ".settings", ".git", "images",
            "webapp", "apidocs", "testapidocs");
    /**
     * The included files.
     */
    static final List<String> INCLUDED_FILES = List.of("pom.xml");
    /**
     * The included extensions.
     */
    static final List<String> INCLUDED_EXTENSIONS = List.of("java", "bat", "yml", "yaml", "html");
    /**
     * The delimiter.
     */
    static final Pattern DELIMITER = Pattern.compile("[^@a-zA-Z]");
    /**
     * The list with the keywords.
     */
    static final List<String> KEYWORDS_LIST = List.of("""
            @COMPONENT
            @CONTROLLER
            @DOCUMENT
            @EJB
            @ENTITY
            @GETMAPPING
            @INJECT
            @MESSAGEDRIVEN
            @NAMEDQUERY
            @POSTMAPPING
            @RESTCONTROLLER
            @SPRINGBOOTAPPLICATION
            @SPRINGBOOTTEST
            @STATELESS
            @TEST
            @TRANSACTIONAL
            @WEBMVCTEST
            ACTIVEMQ
            ANGULARJS
            ASYNCHRONOUS
            BAYES
            CAMUNDA
            CDI
            CHECKSUM
            CIRCUITBREAKER
            COLLATOR
            COMPARABLE
            COMPARATOR
            COMPLETABLEFUTURE
            CREDENTIALS
            CRITERIAQUERY
            CRYPTOGRAPHY
            CURL
            CYCLICBARRIER
            DATABASE
            DECRYPT
            DOCKER
            ELLIPTICCURVECRYPTOGRAPHY
            ENCRYPT
            ENTITYMANAGER
            ENTITYMODEL
            FLUX
            HATEOAS
            JAXB
            JBOSS
            JPA
            JQUERY
            JSF
            KAFKA
            MAPSTRUCT
            MOCKITO
            MOCKMVC
            MONGODB
            MONO
            OPENAPI
            ORCHESTRATION
            PATRICIATRIE
            POSTGRESQL
            REACTIVE
            REACTIVEMONGOREPOSITORY
            REDIS
            RESTFUL
            RESTTEMPLATE
            SECURERANDOM
            SOAP
            SONAR
            SONARQUBE
            SPRING
            SSLSOCKET
            SWAGGER
            TEEING
            TESTRESTTEMPLATE
            THYMELEAF
            WEBCLIENT
            WEBFLUX
            WELD
            WILDFLY
            ZONEDDATETIME
            """.split("\\R+"));

    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}