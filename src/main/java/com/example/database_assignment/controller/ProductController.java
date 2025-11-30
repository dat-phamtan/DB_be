package com.example.database_assignment.controller;

import com.example.database_assignment.dto.ProductRequest;
import com.example.database_assignment.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 1. API Thêm: POST /api/products
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductRequest request) {
        try {
            productService.createProduct(request);
            return ResponseEntity.ok("✅ Thêm thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Lỗi: " + e.getMessage());
        }
    }

    // 2. API Sửa: PUT /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ProductRequest request) {
        try {
            productService.updateProduct(id, request);
            return ResponseEntity.ok("✅ Cập nhật thành công sản phẩm ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Lỗi: " + e.getMessage());
        }
    }

    // 3. API Xóa: DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("✅ Đã xóa sản phẩm ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Lỗi: " + e.getMessage());
        }
    }
}