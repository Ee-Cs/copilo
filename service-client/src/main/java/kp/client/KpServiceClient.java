package kp.client;

import kp.service.KpService;
import kp.utils.Printer;

import java.lang.module.ModuleDescriptor;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.stream.Stream;

/**
 * The {@link KpService} consumer.
 */
public class KpServiceClient {

    /**
     * Private constructor to prevent instantiation.
     */
    private KpServiceClient() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        Printer.printHor();
        Stream.of(KpService.class, KpServiceClient.class)
                .map(Class::getModule).map(Module::getDescriptor).map(ModuleDescriptor::toString)
                .forEach(Printer::print);
        Printer.printHor();

        Printer.print("All provided services:");
        ServiceLoader.load(KpService.class).stream().map(Provider::get)
                .forEach(kpService -> Printer.printf("Service result[%s]%n", kpService.launch()));
        Printer.printHor();

        Printer.print("Only simple service:");
        ServiceLoader.load(KpService.class).stream().map(Provider::get).filter(KpService::isSimple)
                .forEach(kpService -> Printer.printf("Service result[%s]%n", kpService.launch()));
        Printer.printHor();
    }
}