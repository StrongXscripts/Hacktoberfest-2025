# Task Management System

A simple task management system implementation in Python that allows users to create, update, and manage tasks efficiently.

## Features

- Create tasks with title, description, due date, priority, and status
- Update task status
- Remove tasks
- List all tasks
- Get specific task details

## Usage

```python
from task_manager import Task, TaskManager

# Create a task manager
manager = TaskManager()

# Create a task
task = Task("Complete Project", "Finish the Python project", "2025-10-30", "high")

# Add task to manager
task_id = manager.add_task(task)

# Update task status
manager.update_task_status(task_id, "completed")

# List all tasks
tasks = manager.list_tasks()
```

## Running Tests

To run the unit tests:

```bash
python -m unittest test_task_manager.py
```

## Contributing

Feel free to contribute to this project by:
1. Forking the repository
2. Creating a new branch
3. Making your changes
4. Submitting a pull request

## License

This project is licensed under the MIT License.
