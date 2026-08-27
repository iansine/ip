# UI Test Plan

The tests run against the `sine.Sine` main class using Java 25. Each test case is an independent console session, and output is compared exactly after normalizing line endings and the final newline.

## Test case: Manage all task types

Aim: Verify that todo, deadline, and event tasks can be added, listed, marked, and unmarked with the correct UI text.

### Inputs

```text
todo borrow book
deadline return book /by 2026-08-30
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
   [D][ ] return book (by: Aug 30 2026)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Great work! I've marked this task as done:
   [D][X] return book (by: Aug 30 2026)
____________________________________________________________
____________________________________________________________
 Roger that. I've marked this task as not done yet:
   [D][ ] return book (by: Aug 30 2026)
____________________________________________________________
____________________________________________________________
 TODO list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Aug 30 2026)
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
deadline submit report /by 2026-02-30
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
 Error :( Please enter the deadline as yyyy-MM-dd.
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
deadline return book /by 2026-08-30
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
   [D][ ] return book (by: Aug 30 2026)
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
 2.[D][ ] return book (by: Aug 30 2026)
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
deadline return book /by 2026-08-30
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
   [D][ ] return book (by: Aug 30 2026)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Great work! I've marked this task as done:
   [D][X] return book (by: Aug 30 2026)
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

## Test case: Load tasks saved during a previous run

Aim: Verify that todo, deadline, and event tasks and their completion states are loaded from disk when Sine starts.

### Initial data

```text
T | 1 | read book
D | 0 | return book | 2026-08-30
E | 1 | project meeting | Mon 2pm | 4pm
```

### Inputs

```text
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
 TODO list:
 1.[T][X] read book
 2.[D][ ] return book (by: Aug 30 2026)
 3.[E][X] project meeting (from: Mon 2pm to 4pm)
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```

## Test case: Recover from malformed saved data

Aim: Verify that an impossible date in a storage record does not crash Sine and that it starts with an empty task list.

### Initial data

```text
D | 0 | return book | 2026-02-30
```

### Inputs

```text
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
 Warning: I couldn't load your saved tasks. Starting with an empty list.
____________________________________________________________
____________________________________________________________
 TODO list:
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```

## Test case: Ignore blank lines in saved data

Aim: Verify that blank lines in the data file are ignored while valid tasks are still loaded.

### Initial data

```text

T | 0 | read book

D | 1 | return book | 2026-08-30

```

### Inputs

```text
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
 TODO list:
 1.[T][ ] read book
 2.[D][X] return book (by: Aug 30 2026)
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```

## Test case: Load escaped storage characters

Aim: Verify that pipe and backslash characters in task fields are decoded without being mistaken for storage separators.

### Initial data

```text
T | 0 | compare A \| B
D | 1 | use C:\\notes | 2026-09-01
```

### Inputs

```text
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
 TODO list:
 1.[T][ ] compare A | B
 2.[D][X] use C:\notes (by: Sep 01 2026)
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```

## Test case: Find tasks by description

Aim: Verify that find returns description matches in order, renumbers the results, and handles empty results and missing keywords.

### Inputs

```text
todo read book
deadline return book /by 2026-08-30
todo write essay
mark 1
mark 2
find book
find Book
find
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
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Aug 30 2026)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] write essay
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Great work! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Great work! I've marked this task as done:
   [D][X] return book (by: Aug 30 2026)
____________________________________________________________
____________________________________________________________
 Here is what I found:
 1.[T][X] read book
 2.[D][X] return book (by: Aug 30 2026)
____________________________________________________________
____________________________________________________________
 Here is what I found:
____________________________________________________________
____________________________________________________________
 Error :( The search keyword cannot be empty.
____________________________________________________________
____________________________________________________________
 Bye. I'll be here if you need me :)
____________________________________________________________
```
