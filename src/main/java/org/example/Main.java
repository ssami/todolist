package org.example;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.example.models.Todo;
import org.example.storage.DiskStorageGateway;
import org.example.storage.LocalStorageGateway;
import org.example.storage.StorageInterface;
import org.example.utils.Conversion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private String loadMenu() throws URISyntaxException, IOException {
        Path path = Paths.get(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResource("menu.txt"))
                        .toURI());
        Stream<String> lines = Files.lines(path);
        String menu = lines.collect(Collectors.joining("\n"));
        lines.close();

        return menu;
    }

    public static void runInteractive() throws URISyntaxException, IOException {
        Main m = new Main();
        String menu = m.loadMenu();

        LocalStorageGateway gateway = new LocalStorageGateway();

        var scanner = new Scanner(System.in);
        var input = "";
        while (!input.equals("4")) {
            switch (input) {
                case "1" -> {
                    System.out.println("Enter the todo: ");
                    input = scanner.nextLine();
                    var todo = Conversion.parseString(input);
                    gateway.store(todo);
                    System.out.println("Done");
                }
                case "2" -> {
                    Predicate<Todo> findDueToday = t -> {
                        LocalDateTime today = LocalDateTime.now();
                        LocalDateTime todoDue = t.dueBy();
                        return todoDue.getMonth().equals(today.getMonth())
                                && todoDue.getDayOfMonth() == today.getDayOfMonth();
                    };
                    System.out.println(gateway.retrieve(findDueToday));
                }
                default -> System.out.println(menu);
            }
            input = scanner.nextLine();
        }
        gateway.save();
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        ArgumentParser parser = ArgumentParsers.newFor("Main")
                        .build()
                        .defaultHelp(true)
                        .description("Command line interface to todo list builder");
        parser.addArgument("--int")
                .action(Arguments.storeTrue());
        parser.addArgument("--todo")
                .help("Creates a new todo. Format: <string>, <date in dd/mm format (optional)>, <priority (optional)>")
                .dest("todo");
        parser.addArgument("--show")
                .help("Show todos due today")
                .action(Arguments.storeTrue());

        StorageInterface<Todo> diskStorage = new DiskStorageGateway("/tmp/todos.txt");
        TodoEngine todoEngine = new TodoEngine(diskStorage);
        try {
            var results = parser.parseArgs(args);
            if (results.get("int")) {
                Main.runInteractive();
            }
            else if (null != results.get("todo")) {
                String rawTodo = results.get("todo");
                todoEngine.store(rawTodo);
            }
            else if (results.get("show")) {
                // TODO: implement for show for specific date
                List<Todo> dueToday = todoEngine.retrieveTodayList();
                for (int i=0; i<dueToday.size(); i++) {
                    System.out.printf("%d: %s%n", i, dueToday.get(i).thingToDo());
                }
            }
        } catch (ArgumentParserException e) {
            throw new RuntimeException(e);
        } finally {
            todoEngine.save();  // writes out to disk
        }
    }
}
