package com.wingify.controller;
import com.wingify.model.Category;
import com.wingify.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/categories") @RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categories;
    @GetMapping public List<Category> all() { return categories.findAll(); }
}
