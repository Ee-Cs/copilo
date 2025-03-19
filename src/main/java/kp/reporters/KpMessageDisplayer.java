package kp.reporters;

import kp.models.Department;
import kp.models.Employee;
import kp.models.Information;
import org.apache.pulsar.client.api.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.ZoneOffset;

import static kp.Constants.THIN_LINE;

/**
 * Displays the {@link Message}.
 */
public class KpMessageDisplayer {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Default constructor.
     */
    public KpMessageDisplayer() {
        // constructor is empty
    }

    /**
     * Displays the {@link Message}.
     *
     * @param message the {@link Message}
     */
    void displayMessage(Message<Information> message) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.lineSeparator()).append(THIN_LINE).append(System.lineSeparator());
        stringBuilder.append("message:").append(System.lineSeparator());
        stringBuilder.append("topic[%s],%n".formatted(message.getTopicName()));
        stringBuilder.append("key[%s], ".formatted(message.getKey()));
        stringBuilder.append("sequenceId[%s], ".formatted(message.getSequenceId()));
        stringBuilder.append("messageId[%s],%n".formatted(message.getMessageId()));
        stringBuilder.append("publishTime[%tT.%<tL] 'UTC'%n".formatted(
                Instant.ofEpochMilli(message.getPublishTime()).atOffset(ZoneOffset.UTC)));
        displayInformation(stringBuilder, message.getValue());
        stringBuilder.append(THIN_LINE);
        logger.info("displayMessage(): {}", stringBuilder);
    }

    /**
     * Displays the {@link Information}.
     *
     * @param stringBuilder the {@link StringBuilder}
     * @param information   the {@link Information}
     */
    void displayInformation(StringBuilder stringBuilder, Information information) {

        stringBuilder.append("information:%n- id[%d], label[%s], approvalStatus[%s],%n".formatted(
                information.getId(), information.getLabel(), information.getApprovalStatus()));
        information.getDepartments().forEach(department -> displayDepartment(stringBuilder, department));
    }

    /**
     * Displays the {@link Department}.
     *
     * @param stringBuilder the {@link StringBuilder}.
     * @param department    the {@link Department}
     */
    private void displayDepartment(StringBuilder stringBuilder, Department department) {

        stringBuilder.append("- department:%n- - id[%d], name[%s],%n".formatted(
                department.getId(), department.getName()));
        stringBuilder.append("- - money[%s], createdAt[%tT.%<tL]%n".formatted(
                department.getMoney(), department.getCreatedAt()));
        department.getEmployees().forEach(employee -> displayEmployee(stringBuilder, employee));
    }

    /**
     * Displays the {@link Employee}.
     *
     * @param stringBuilder the {@link StringBuilder}.
     * @param employee      the {@link Employee}
     */
    private void displayEmployee(StringBuilder stringBuilder, Employee employee) {

        stringBuilder.append("- - employee:%n- - - id[%d], firstName[%s], lastName[%s], title[%s]%n".formatted(
                employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getTitle()));
    }

}
