# Why? 

I got tired of opening up Notes or Evernote or Doc, navigating to my todo list and making changes. Something on the command line can be easily done with keyboard shortcuts, which I prefer while I'm navigating around my code base.  

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

Similarly, to show incomplete todos: 
```shell
java -jar TodoList-1.0-SNAPSHOT.jar --loc "/Users/sumitasami/todos.txt" --show
```

High priority incomplete todos:
```shell
java -jar TodoList-1.0-SNAPSHOT.jar --loc "/Users/sumitasami/todos.txt" --top
```

And delete: 

```shell
java -jar TodoList-1.0-SNAPSHOT.jar --loc "/Users/sumitasami/todos.txt" --delete <index of todo to be deleted> 
```

To use, I create shortcuts for these commands in my ZSH profile, e.g. 
```shell
alias todo=/Users/me/scripts/todo.sh 
```
where the .sh script is just one of those commands above that takes in user input $1. 