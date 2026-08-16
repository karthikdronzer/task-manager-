package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long id) {
        Task task = findTaskOrThrow(id);
        return TaskResponse.fromEntity(task);
    }

    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        applyRequestToTask(request, task);
        Task saved = taskRepository.save(task);
        return TaskResponse.fromEntity(saved);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = findTaskOrThrow(id);
        applyRequestToTask(request, task);
        Task updated = taskRepository.save(task);
        return TaskResponse.fromEntity(updated);
    }

    public void deleteTask(Long id) {
        Task task = findTaskOrThrow(id);
        taskRepository.delete(task);
    }

    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> searchTasksByTitle(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public TaskResponse markComplete(Long id) {
        Task task = findTaskOrThrow(id);
        task.setStatus(TaskStatus.COMPLETED);
        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    private void applyRequestToTask(TaskRequest request, Task task) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING);
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
    }
}
