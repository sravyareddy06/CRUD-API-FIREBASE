package com.example.crudfirebase.Controller;

import com.example.crudfirebase.Service.CRUDService;
import com.example.crudfirebase.Entity.CRUD;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class CRUDController {

    private final CRUDService crudService;

    public CRUDController(CRUDService crudService) {
        this.crudService = crudService;
    }

    @GetMapping("/")
    public String index(Model model) throws ExecutionException, InterruptedException {
        List<CRUD> todoList = crudService.getAllCRUD(); // Fetch all To-Do items from Firestore
        model.addAttribute("todoList", todoList); // Add the list to the model
        return "index";
    }


    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("newToDo", new CRUD());
        return "create";
    }

    @PostMapping("/create")
    public String createCRUD(@ModelAttribute("newToDo") CRUD crud) throws ExecutionException, InterruptedException {
        crudService.createCRUD(crud);
        return "redirect:/";  // Redirect to the home page after creating the To-Do item
    }

    @GetMapping("/update/{id}")
    public String showUpdatePage(@PathVariable String id, Model model) throws ExecutionException, InterruptedException {
        CRUD existingToDo = crudService.getCRUD(id);
        model.addAttribute("existingToDo", existingToDo);
        return "update";
    }

    @PostMapping("/update")
    public String updateCRUD(@ModelAttribute("existingToDo") CRUD updatedToDo) throws ExecutionException, InterruptedException {
        crudService.updateCRUD(updatedToDo);
        return "redirect:/";  // Redirect to the home page after updating the To-Do item
    }

    @GetMapping("/delete/{id}")
    public String deleteCRUD(@PathVariable String id) throws ExecutionException, InterruptedException {
        crudService.deleteCRUD(id);
        return "redirect:/";  // Redirect to the home page after deleting the To-Do item
    }
}
