package ru.skypro.homework.filter.controller;

     import org.springframework.web.bind.annotation.*;
     import ru.skypro.homework.filter.model.FilterDto;

     import java.util.Collections;
     import java.util.List;

     @RestController
     @RequestMapping("/filters")
     public class FilterController {

         @GetMapping
         public List<FilterDto> getFilters(@RequestParam(required = false) String type) {
             return Collections.emptyList();
         }

         @PostMapping
         public FilterDto createFilter(@RequestBody FilterDto filterDto) {
             return filterDto;
         }

         @PutMapping("/{id}")
         public FilterDto updateFilter(@PathVariable Long id, @RequestBody FilterDto filterDto) {
             return filterDto;
         }

         @DeleteMapping("/{id}")
         public void deleteFilter(@PathVariable Long id) {
         }
     }