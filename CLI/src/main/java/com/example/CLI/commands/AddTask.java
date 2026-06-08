 package com.example.CLI.commands;

import com.example.CLI.commands.helpers.Id;
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

import com.example.CLI.commands.helpers.Id.*;

@ShellComponent
public class AddTask {

    final File file = new File("C:/Users/Shreyansh.Patil/IdeaProjects/Spring-and-Sprinboot/CLI/src/main/resources/tasks.json");
    public Id helperId = new Id();

    @ShellMethod(key = "tasks add", value = "Adds tasks to the list")
    public String addTask(@ShellOption String description, @ShellOption(defaultValue = "todo") String status, @ShellOption String createdAt, @ShellOption String updatedAt)
            throws IOException {

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

            int id = 0;

            if (tasksNode instanceof ArrayNode tasksArray) {
                int newId = helperId.getId(tasksNode) + 1;
                id = newId;
                ObjectNode newTask = mapper.createObjectNode();
                newTask.put("id", newId);
                newTask.put("description", description);
                newTask.put("status", status);
                newTask.put("created_at", createdAt);
                newTask.put("updated_at", updatedAt);

                tasksArray.add(newTask);
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

            helperId.setId(id+1);

        } catch (IOException e) {
            System.err.println("Error reading or writing JSON: " + e.getMessage());
        }

        return "\"" + description + "\"" + " added to tasks list";
    }
}

