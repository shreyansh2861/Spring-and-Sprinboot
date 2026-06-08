package com.example.CLI.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.IOException;

@ShellComponent
public class DeleteTask {

    final File file = new File("C:\\Users\\Shreyansh.Patil\\IdeaProjects\\Spring-and-Sprinboot\\CLI\\src\\main\\resources\\tasks.json");

    @ShellMethod(key = "tasks delete", value = "Delete tasks using the name")
    public String deleteTask(@ShellOption(help = "Task name to delete") String name) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try{
            if (!file.exists()) {
                return "Input file not found. No tasks available. To add new tasks please use `tasks-add taskName taskDescription`";
            }
            JsonNode rootNode = mapper.readTree(file);
            JsonNode tasksNode = rootNode.get("tasks");

            if (tasksNode == null || !tasksNode.isArray()) {
                return "No Tasks in the file to delete";
            }

            ArrayNode newTasksArray = mapper.createArrayNode();

            for(JsonNode task : tasksNode){
                if(!task.get("name").asText().equals(name)){
                    ObjectNode t = mapper.createObjectNode();
                    t.put("name", task.get("name").asText());
                    t.put("description", task.get("description").asText());

                    newTasksArray.add(t);
                }
            }

            ((ObjectNode) rootNode).set("tasks", newTasksArray);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

            System.out.println("Tasks deleted successfully.");
        }
        catch(IOException e){
            System.err.println("Error reading or writing JSON: " + e.getMessage());
        }

        return name + " deleted from tasks list";
    }
}
