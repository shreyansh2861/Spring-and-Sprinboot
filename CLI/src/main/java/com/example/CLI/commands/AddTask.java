package com.example.CLI.commands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

@ShellComponent
public class AddTask {


    final File file = new File("C:\\Users\\Shreyansh.Patil\\IdeaProjects\\Spring-and-Sprinboot\\CLI\\src\\main\\resources\\tasks.json");

    @ShellMethod(key = "tasks-add", value = "Adds tasks to the list")
    public String addTask(@ShellOption(defaultValue = "EmptyTask") String name, @ShellOption String description)
            throws IOException {
//        File file = new File("C:\\Users\\Shreyansh.Patil\\IdeaProjects\\Spring-and-Sprinboot\\CLI\\src\\main\\resources\\tasks.json");

        // Create ObjectMapper (thread-safe after configuration)
        ObjectMapper mapper = new ObjectMapper();

        try {
            if (!file.exists()) {
                System.out.println("Input file not found. Creating new JSON structure.");
                ObjectNode root = mapper.createObjectNode();
                ArrayNode tasks = mapper.createArrayNode();
                root.set("tasks", tasks);

                mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            }
            JsonNode rootNode = mapper.readTree(file);
            JsonNode tasksNode = rootNode.get("tasks");

            if (tasksNode == null || !tasksNode.isArray()) {
                System.out.println("Invalid JSON format: 'tasks' array is missing. Creating it.");
                ObjectNode newRoot = mapper.createObjectNode();
                tasksNode = mapper.createArrayNode();
                newRoot.set("tasks", tasksNode);
                rootNode = newRoot;
            }

            if (tasksNode instanceof ArrayNode tasksArray) {
                ObjectNode newTask = mapper.createObjectNode();
                newTask.put("name", name);
                newTask.put("description", description);

                tasksArray.add(newTask);
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

            System.out.println("Tasks added successfully.");
        } catch (IOException e) {
            System.err.println("Error reading or writing JSON: " + e.getMessage());
        }

        return name + " added to tasks list";
    }
}

