package org.example.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class TodoTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final Priority priority = Priority.HIGH;

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create todo with valid parameters")
        void shouldCreateTodoWithValidParameters() {
            Todo todo = new Todo("Test todo", now, priority);
            assertEquals("Test todo", todo.thingToDo());
            assertEquals(now, todo.dueBy());
            assertEquals(priority, todo.priority());
            assertFalse(todo.isCompleted());
            assertNull(todo.completedAt());
        }

        @Test
        @DisplayName("Should throw exception when thingToDo is null")
        void shouldThrowExceptionWhenThingToDoIsNull() {
            assertThrows(NullPointerException.class, () -> 
                new Todo(null, now, priority)
            );
        }

        @Test
        @DisplayName("Should throw exception when thingToDo is blank")
        void shouldThrowExceptionWhenThingToDoIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> 
                new Todo("   ", now, priority)
            );
        }
    }

    @Nested
    @DisplayName("copyWith Tests")
    class CopyWithTests {
        private final Todo originalTodo = new Todo("Original todo", now, priority);

        @Test
        @DisplayName("Should create new todo with updated fields")
        void shouldCreateNewTodoWithUpdatedFields() {
            LocalDateTime newDueBy = now.plusDays(1);
            Priority newPriority = Priority.MEDIUM;
            
            Todo updatedTodo = originalTodo.copyWith("Updated todo", newDueBy, newPriority, true, now);
            
            assertEquals("Updated todo", updatedTodo.thingToDo());
            assertEquals(newDueBy, updatedTodo.dueBy());
            assertEquals(newPriority, updatedTodo.priority());
            assertTrue(updatedTodo.isCompleted());
            assertEquals(now, updatedTodo.completedAt());
        }

        @Test
        @DisplayName("Should keep original values when fields are null")
        void shouldKeepOriginalValuesWhenFieldsAreNull() {
            Todo updatedTodo = originalTodo.copyWith(null, null, null, null, null);
            
            assertEquals(originalTodo.thingToDo(), updatedTodo.thingToDo());
            assertEquals(originalTodo.dueBy(), updatedTodo.dueBy());
            assertEquals(originalTodo.priority(), updatedTodo.priority());
            assertEquals(originalTodo.isCompleted(), updatedTodo.isCompleted());
            assertEquals(originalTodo.completedAt(), updatedTodo.completedAt());
        }

        @Test
        @DisplayName("Should validate non-null thingToDo")
        void shouldValidateNonNullThingToDo() {
            assertThrows(IllegalArgumentException.class, () -> 
                originalTodo.copyWith("   ", now, priority, true, now)
            );
        }
    }

    @Nested
    @DisplayName("toString Tests")
    class ToStringTests {
        @Test
        @DisplayName("Should return correct string for incomplete todo")
        void shouldReturnCorrectStringForIncompleteTodo() {
            Todo todo = new Todo("Test todo", now, priority);
            String expected = String.format("Test todo,%d/%d,HIGH", 
                now.getMonthValue(), now.getDayOfMonth());
            assertEquals(expected, todo.toString());
        }

        @Test
        @DisplayName("Should return correct string for completed todo")
        void shouldReturnCorrectStringForCompletedTodo() {
            Todo todo = new Todo("Test todo", now, priority, true, now);
            String expected = String.format("Test todo,%d/%d,HIGH,true,%s", 
                now.getMonthValue(), now.getDayOfMonth(), now);
            assertEquals(expected, todo.toString());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {
        @Test
        @DisplayName("Should be equal when all fields are the same")
        void shouldBeEqualWhenAllFieldsAreTheSame() {
            Todo todo1 = new Todo("Test todo", now, priority);
            Todo todo2 = new Todo("Test todo", now, priority);
            assertEquals(todo1, todo2);
            assertEquals(todo1.hashCode(), todo2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when fields are different")
        void shouldNotBeEqualWhenFieldsAreDifferent() {
            Todo todo1 = new Todo("Test todo 1", now, priority);
            Todo todo2 = new Todo("Test todo 2", now, priority);
            assertNotEquals(todo1, todo2);
            assertNotEquals(todo1.hashCode(), todo2.hashCode());
        }
    }
}
