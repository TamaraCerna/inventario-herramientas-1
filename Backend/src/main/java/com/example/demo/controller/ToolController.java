package com.example.demo.controller;

import com.example.demo.entity.ToolEntity;
import com.example.demo.entity.enums.StateTool;
import com.example.demo.service.LoanService;
import com.example.demo.service.ToolService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
@CrossOrigin(origins = "http://localhost:5173")
public class ToolController {

    @Autowired
    private ToolService toolService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private UserService userService;

    // RF1.1
    @GetMapping
    public List<ToolEntity> getToolsLoan() {
        return loanService.getTools();
    }

    // RF1.1
    @PostMapping("/register")
    public ResponseEntity<ToolEntity> registerTool(
            @RequestParam String name_tool,
            @RequestParam StateTool category_tool,
            @RequestParam int replacement_value_tool,
            @RequestParam int userId) {

        ToolEntity tool = toolService.registerNewTool(
                name_tool,
                category_tool,
                replacement_value_tool,
                userId
        );
        return ResponseEntity.ok(tool);
    }

    // RF1.2
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteTool(@PathVariable int id) {
        toolService.deleteToolById(id);
    }
}


