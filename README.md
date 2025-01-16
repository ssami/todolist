# Usage

Single command, non-interactive:

```shell
java -jar TodoList-1.0-SNAPSHOT.jar --loc "/Users/sumitasami/todos.txt" --todo "clean garage"
```
Stores into desired location as long as the program has permissions for it. 

Interactive, for testing: 

```shell
java -jar TodoList-1.0-SNAPSHOT.jar --int --loc 
```
Stores into `/tmp/todos.txt`