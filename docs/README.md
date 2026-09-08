# Sine User Guide

Sine manages todo, deadline, and event tasks through a console or chat window.
The startup message lists the available commands.

## Undoing task changes

Type exactly `undo` or `UNDO`, with no arguments, to reverse the most recent
remaining task change in this session. Repeat the command to undo earlier
changes. There is no configured history limit and no redo command.

Undo supports adding any task type, deleting tasks, marking tasks, and
unmarking tasks. A deleted task returns to its original position with its
original details and completion status. Task numbers follow the restored order.

`list`, `find`, invalid commands, and commands that leave a task unchanged
(such as marking an already completed task) do not consume undo history.
For example, `todo book`, `list`, then `undo` removes the book task.

### Responses

After `deadline book /by 2026-08-30`, typing `undo` gives:

```text
Understood. I undid the last task change.
I removed this task:
  [D][ ] book (by: Aug 30 2026)
```

Every undo starts with the same acknowledgement. The following line explains
the action: `I removed this task:`, `I added back this task:`,
`I unmarked this task:`, or `I marked this task as done:`. The task line
includes its type, completion status, description, and any date/time fields
in the usual display format. The console adds its normal separators; the GUI
appends a reply and retains the earlier conversation.

If no change remains to undo:

```text
Error :( There is nothing to undo.
```

Mixed case (`Undo`), extra arguments (`undo 2`), and other malformed undo
commands receive:

```text
Error :( Usage: undo or UNDO
```

Words such as `undoable`, `undo!`, and `redo` remain unknown commands. The GUI
removes surrounding whitespace before processing input; the console requires
the exact spelling without surrounding whitespace.

### History and saving

Only changes made in the current session can be undone. Closing and reopening
the application clears history, even though saved tasks remain. Undo itself
never enters history. After an accidental undo, manually make the desired
change again. A new change after undo can itself be undone without losing
older history. In the GUI, `bye` displays a farewell without closing the window
or clearing the session.

Undo saves the restored task list immediately using the existing
`data/sine.txt` format; no migration or history file is needed. Existing task
files remain compatible. Use one application instance per data file; external
file edits are not reconciled with the running session.

If an ordinary change fails to save, it remains in memory and is still
undoable. If undo fails to save, it still restores memory and consumes its
history entry. The undo response is followed by the existing warning:

```text
 Error :( I couldn't save that change. It is available only for this session.
```

The console places its normal separator after the undo response and again
after the warning. The GUI shows both in the same reply. A failed write does
not guarantee the previous file contents are intact.
