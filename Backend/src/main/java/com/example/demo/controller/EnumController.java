package com.example.demo.controller;

import com.example.demo.entity.enums.StateTool;
import com.example.demo.entity.enums.State;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enums")
public class EnumController {

    // StateTool
    @GetMapping("/tools")
    public ResponseEntity<StateTool[]> getToolCategories() {
        return ResponseEntity.ok(StateTool.values());
    }

    // State
    @GetMapping("/states")
    public ResponseEntity<State[]> getToolStates() {
        return ResponseEntity.ok(State.values());
    }
}
