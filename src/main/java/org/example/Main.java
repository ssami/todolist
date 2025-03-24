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
//        Path path = Paths.get(
//                Objects.requireNonNull(
//                        getClass().getClassLoader()
//                                .getResource("menu.txt"))
//                        .toURI());
//        Stream<String> lines = Files.lines(path);
//        String menu = lines.collect(Collectors.joining("\n"));
//        lines.close();

        // TODO: for some reason, retrieving menu.txt from src/main/resources is not working anymore in the JAR file

        return "What do you want to do?\n" +
                "1. Create a new todo in the format <todo string>, <date due in mm/dd>{optional}, " +
                "<priority in format HIGH, MEDIUM, LOW, TOP3>{optional}\n" +
                "2. List todos for today\n" +
                "3. List the top todos for today\n" +
                "4. List this menu again\n" +
                "5. Exit";
    }

    public static void runInteractive() throws URISyntaxException, IOException {
        Main m = new Main();
        String menu = m.loadMenu();

        LocalStorageGateway gateway = new LocalStorageGateway();

        var scanner = new Scanner(System.in);
        var input = "";
        while (!input.equals("5")) {
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
                case "3" -> {
                    System.out.println("not implemented");
                }
                default -> System.out.println(menu);
            }
            input = scanner.nextLine();
        }
        gateway.save();
    }

    private static void printTodos(List<Todo> todos) {
        System.out.println("\n\n");
        System.out.println("*********************************************");
        System.out.println("\n");
        for (int i=0; i<todos.size(); i++) {
            System.out.printf("%d: %s%n", i, todos.get(i).thingToDo());
        }
        System.out.println("\n");
        System.out.println("*********************************************");
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
        parser.addArgument("--top")
                .help("Show top priority todos")
                .action(Arguments.storeTrue());
        parser.addArgument("--delete")
                .help("Delete the index of the todo")
                .dest("delete");
        parser.addArgument("--loc")
                .help("Location for storing the todos")
                .dest("loc");

        TodoEngine todoEngine = new TodoEngine();
        String defaultLocation = "/tmp/todos.txt";
        StorageInterface<Todo> defaultDiskStorage = new DiskStorageGateway(defaultLocation);
        todoEngine.setTodoStorageGateway(defaultDiskStorage);
        try {
            var results = parser.parseArgs(args);
            String location = "";
            if (null != results.get("loc")) {
                location = results.get("loc");
                StorageInterface<Todo> diskStorage = new DiskStorageGateway(location);
                todoEngine.setTodoStorageGateway(diskStorage);
            }
            if (results.get("int")) {
                Main.runInteractive();
            }
            else if (null != results.get("todo")) {
                String rawTodo = results.get("todo");
                todoEngine.store(rawTodo);
            }
            else if (results.get("show")) {
                // TODO: implement for show for specific date
                List<Todo> dueToday = todoEngine.retrieveUndoneList();
                printTodos(dueToday);
            }
            else if (results.get("top")) {
                List<Todo> topTodos = todoEngine.retrieveTopPriorityList();
                printTodos(topTodos);
            }
            else if (null != results.get("delete")) {
                int toDel = Integer.parseInt(results.get("delete"));
                todoEngine.remove(toDel);
                List<Todo> remaining = todoEngine.retrieveUndoneList();
                for (int i=0; i<remaining.size(); i++) {
                    System.out.printf("%d: %s%n", i, remaining.get(i).thingToDo());
                }
            }
        } catch (ArgumentParserException e) {
            throw new RuntimeException(e);
        } finally {
            todoEngine.save();  // writes out to disk
        }
    }
}
