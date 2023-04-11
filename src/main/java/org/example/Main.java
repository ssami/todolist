package org.example;

import org.example.models.Priority;
import org.example.models.Todo;
import org.example.storage.StorageGateway;
import org.example.utils.Conversion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private String loadMenu() throws URISyntaxException, IOException {
        Path path = Paths.get(
                Objects.requireNonNull(getClass().getResource("resources/menu.txt")).toURI());
        Stream<String> lines = Files.lines(path);
        String menu = lines.collect(Collectors.joining("\n"));
        lines.close();

        return menu;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        // load existing todos
        // menu loop
        // if reading in new todo, create a todo object
        // when exit, save todos into file (or save regularly in the background)

        Main m = new Main();
        String menu = m.loadMenu();

        StorageGateway gateway = new StorageGateway();

        var scanner = new Scanner(System.in);
        var input = "";
        while (!input.equals("4")) {
            switch(input) {
                case "1" -> {
                    System.out.println("Enter the todo: ");
                    input = scanner.nextLine();
                    var todo = new Todo(input, Conversion.validDate("3/3"), Priority.HIGH);
                    gateway.store(todo);
//                    System.out.println(todo);
                }
                case "2" -> {
                    System.out.println(gateway.retrieve());
                }
                default -> System.out.println(menu);
            }
            input = scanner.nextLine();
        }
    }
}
