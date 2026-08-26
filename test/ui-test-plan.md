# UI Test Plan

The tests run against the `Sine` main class using Java 25. Each test case is an independent console session, and output is compared exactly after normalizing line endings and the final newline.

## Test case: Manage all task types

Aim: Verify that todo, deadline, and event tasks can be added, listed, marked, and unmarked with the correct UI text.

### Inputs

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 2
unmark 2
list
bye
```

### Expected output

```text
____________________________________________________________
 ____  _            
/ ___|(_)_ __   ___ 
\___ \| | '_ \ / _ \
 ___) | | | | |  __/
|____/|_|_| |_|\___|
Hello! I'm Sine.
What's up?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Great work! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Roger that. I've marked this task as not done yet:
   [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 TODO list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to 4pm)
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```

## Test case: Reject malformed commands with exceptions

Aim: Verify that malformed task and status commands report chatbot-specific errors and allow the session to continue.

### Inputs

```text
todo
deadline submit report
event project meeting /from Monday
mark abc
mark 1
bye
```

### Expected output

```text
____________________________________________________________
 ____  _            
/ ___|(_)_ __   ___ 
\___ \| | '_ \ / _ \
 ___) | | | | |  __/
|____/|_|_| |_|\___|
Hello! I'm Sine.
What's up?
____________________________________________________________
____________________________________________________________
 Error :( The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
 Error :( The deadline of a deadline cannot be empty.
____________________________________________________________
____________________________________________________________
 Error :( The start time of an event cannot be empty.
____________________________________________________________
____________________________________________________________
 Error :( Please enter a valid task number.
____________________________________________________________
____________________________________________________________
 Error :( That task number does not exist.
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```

## Test case: Delete a task

Aim: Verify that deleting a task removes the selected item and updates the task count and subsequent list.

### Inputs

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
delete 3
list
bye
```

### Expected output

```text
____________________________________________________________
 ____  _            
/ ___|(_)_ __   ___ 
\___ \| | '_ \ / _ \
 ___) | | | | |  __/
|____/|_|_| |_|\___|
Hello! I'm Sine.
What's up?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Roger that. I've removed this task:
   [E][ ] project meeting (from: Mon 2pm to 4pm)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 TODO list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```

## Test case: Reject an unknown command

Aim: Verify that an unrecognized command is not added as a task and receives the fallback response.

### Inputs

```text
blah
bye
```

### Expected output

```text
____________________________________________________________
 ____  _            
/ ___|(_)_ __   ___ 
\___ \| | '_ \ / _ \
 ___) | | | | |  __/
|____/|_|_| |_|\___|
Hello! I'm Sine.
What's up?
____________________________________________________________
____________________________________________________________
 Must have been the wind
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```

## Test case: Save the latest task-list state

Aim: Verify that commands which add, mark, and delete tasks still complete normally while the latest task-list state is saved to disk.

### Inputs

```text
todo borrow book
deadline return book /by Sunday
mark 2
delete 1
bye
```

### Expected output

```text
____________________________________________________________
 ____  _            
/ ___|(_)_ __   ___ 
\___ \| | '_ \ / _ \
 ___) | | | | |  __/
|____/|_|_| |_|\___|
Hello! I'm Sine.
What's up?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Great work! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Roger that. I've removed this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```
