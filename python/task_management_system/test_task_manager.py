import unittest
from task_manager import Task, TaskManager

class TestTaskManager(unittest.TestCase):
    def setUp(self):
        self.manager = TaskManager()

    def test_add_task(self):
        task = Task("Test Task", "Test Description")
        task_id = self.manager.add_task(task)
        self.assertEqual(task_id, 0)
        self.assertEqual(len(self.manager.tasks), 1)

    def test_remove_task(self):
        task = Task("Test Task", "Test Description")
        task_id = self.manager.add_task(task)
        self.assertTrue(self.manager.remove_task(task_id))
        self.assertEqual(len(self.manager.tasks), 0)

    def test_update_task_status(self):
        task = Task("Test Task", "Test Description")
        task_id = self.manager.add_task(task)
        self.assertTrue(self.manager.update_task_status(task_id, "completed"))
        self.assertEqual(self.manager.get_task(task_id).status, "completed")

if __name__ == '__main__':
    unittest.main()
