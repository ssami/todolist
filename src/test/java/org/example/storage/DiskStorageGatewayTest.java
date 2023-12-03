package org.example.storage;

import org.example.models.Priority;
import org.example.models.Todo;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class DiskStorageGatewayTest {

    @Test
    public void test_init() throws URISyntaxException, IOException {
        // Act
        String filePath = "/tmp/test_todos_" + System.currentTimeMillis() + ".txt";
        new DiskStorageGateway(filePath);
        Path storePath = Path.of(filePath);

        // Validate
        Assert.assertTrue(Files.exists(storePath));

        // Clean up
        Files.delete(storePath);
    }

    @Test
    public void test_store() throws URISyntaxException, IOException {
        // Act
        String filePath = "/tmp/test_todos_" + System.currentTimeMillis() + ".txt";
        DiskStorageGateway gateway = new DiskStorageGateway(filePath);
        gateway.store(new Todo("test this method", LocalDateTime.now(), Priority.HIGH));
        gateway.save();

        // Validate
        Path storePath = Path.of(filePath);
        Assert.assertTrue(Files.exists(storePath));
        List<String> contents = Files.readAllLines(storePath);
        Assert.assertEquals(1, contents.size());
        Assert.assertTrue(contents.get(0).contains("test this method"));
    }


}
