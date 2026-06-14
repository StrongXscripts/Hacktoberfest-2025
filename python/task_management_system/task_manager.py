class Task:
    def __init__(self, title, description, due_date=None, priority="medium", status="pending"):
        self.title = title
        self.description = description
        self.due_date = due_date
        self.priority = priority
        self.status = status

    def __str__(self):
        return f"Task: {self.title} (Status: {self.status}, Priority: {self.priority})"

class TaskManager:
    def __init__(self):
        self.tasks = []

    def add_task(self, task):
        self.tasks.append(task)
        return len(self.tasks) - 1  # Return task ID

    def remove_task(self, task_id):
        if 0 <= task_id < len(self.tasks):
            del self.tasks[task_id]
            return True
        return False

    def update_task_status(self, task_id, new_status):
        if 0 <= task_id < len(self.tasks):
            self.tasks[task_id].status = new_status
            return True
        return False

    def list_tasks(self):
        return [(i, task) for i, task in enumerate(self.tasks)]

    def get_task(self, task_id):
        if 0 <= task_id < len(self.tasks):
            return self.tasks[task_id]
        return None
