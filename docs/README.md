# ChioChat — User Guide

ChioChat is a simple task-tracking chatbot with a clean, asymmetric chat UI. Type commands into the input box and press Enter or click Send. ChioChat will reply in the chat. Type `bye` to exit.

## Quick Start
- Launch the app. You will see a greeting from ChioChat.
- Enter a command (see below) and press Enter.
- Tasks are saved automatically; you can close the app anytime.

## Commands Overview
(See the in-app `help` command or the command map in `CommandManager` for the authoritative list.)

- list: Show all tasks
  - Example: `list`

- todo <desc>: Add a ToDo task
  - Example: `todo Read CS2103T text`

- deadline <desc> /by <time>: Add a Deadline task
  - Example: `deadline Submit iP /by 2025-09-30 23:59`

- event <desc> /from <start> /to <end>: Add an Event task
  - Example: `event Hackathon /from 2025-10-05 10:00 /to 2025-10-06 18:00`

- delete <id>: Delete a task by its listed ID (1-based)
  - Example: `delete 2`

- mark <id>: Mark a task as done
  - Example: `mark 3`

- unmark <id>: Mark a task as not done
  - Example: `unmark 3`

- find <keyword>: Search tasks containing a keyword
  - Example: `find iP`

- help: Show the help menu with all commands
  - Example: `help`

## Tips
- IDs shown in `list` start from 1.
- If a command needs more details, ChioChat will let you know.
- Type `bye` to close the app.