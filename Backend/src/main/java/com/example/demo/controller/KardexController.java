package com.example.demo.controller;

import com.example.demo.entity.KardexEntity;
import com.example.demo.entity.ToolEntity;
import com.example.demo.service.KardexService;
import com.example.demo.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/kardex")
public class KardexController {

    @Autowired
    KardexService kardexService;
    @Autowired
    ToolService toolService;

    // RF5.2
    @GetMapping("/history/{toolId}")
    public ResponseEntity<List<KardexEntity>> getToolHistory(@PathVariable int toolId) {
        ToolEntity tool = toolService.findById(toolId);
        List<KardexEntity> history = kardexService.historyKardex(tool);
        return ResponseEntity.ok(history);
    }

    // RF5.3
    @GetMapping("/search")
    public ResponseEntity<List<KardexEntity>> searchKardex(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate init,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<KardexEntity> result = kardexService.seachKardex(init, end);
        return ResponseEntity.ok(result);
    }
}
