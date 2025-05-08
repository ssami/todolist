package org.example;

import org.example.models.Priority;
import org.example.models.Todo;
import org.example.storage.StorageInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TodoEngine Tests")
@ExtendWith(MockitoExtension.class)
class TodoEngineTest {

    @Mock
    private StorageInterface<Todo> mockStorage;

    private TodoEngine todoEngine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todoEngine = new TodoEngine(mockStorage);
    }

    @Test
    @DisplayName("Should complete a todo successfully")
    void testCompleteTodo() {
        // Setup
        LocalDateTime now = LocalDateTime.now();
        Todo originalTodo = new Todo("Test task", now, Priority.HIGH);
        List<Todo> todos = new ArrayList<>();
        todos.add(originalTodo);
        
        when(mockStorage.retrieve(any())).thenReturn(todos);
        
        // Execute
        todoEngine.completeTodo(0);
        
        // Verify
        verify(mockStorage).remove(0);
        verify(mockStorage).store(argThat(todo -> 
            todo.thingToDo().equals("Test task") &&
            todo.isCompleted() &&
            todo.completedAt() != null
        ));
    }

    @Test
    @DisplayName("Should throw exception when completing invalid todo index")
    void testCompleteTodoInvalidIndex() {
        when(mockStorage.retrieve(any())).thenReturn(new ArrayList<>());
        
        assertThrows(IllegalArgumentException.class, () -> 
            todoEngine.completeTodo(0));
    }

    @Test
    @DisplayName("Should retrieve todos completed yesterday")
    void testRetrieveCompletedYesterday() {
        // Setup
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        Todo completedTodo = new Todo("Test task", LocalDateTime.now(), Priority.HIGH, true, yesterday);
        List<Todo> todos = new ArrayList<>();
        todos.add(completedTodo);
        
        when(mockStorage.retrieve(any())).thenReturn(todos);
        
        // Execute
        List<Todo> result = todoEngine.retrieveCompletedYesterday();
        
        // Verify
        assertEquals(1, result.size());
        assertEquals(completedTodo, result.get(0));
    }

    @Test
    @DisplayName("Should retrieve all completed todos")
    void testRetrieveCompletedList() {
        // Setup
        Todo completedTodo = new Todo("Test task", LocalDateTime.now(), Priority.HIGH, true, LocalDateTime.now());
        List<Todo> todos = new ArrayList<>();
        todos.add(completedTodo);
        
        when(mockStorage.retrieve(any())).thenReturn(todos);
        
        // Execute
        List<Todo> result = todoEngine.retrieveCompletedList();
        
        // Verify
        assertEquals(1, result.size());
        assertEquals(completedTodo, result.get(0));
    }
} 