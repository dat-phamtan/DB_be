package com.example.database_assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductSearchController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/search")
    public List<Map<String, Object>> search(
            @RequestParam Integer storeId,
            @RequestParam(required = false) String tenSanPham,
            @RequestParam(required = false) String tenDanhMuc,
            @RequestParam(required = false) BigDecimal giaTu,
            @RequestParam(required = false) BigDecimal giaDen,
            @RequestParam(required = false, defaultValue = "Ngay_dang_DESC") String sapXep
    ) {
        String sql = "EXEC sp_TimKiemSanPham_CuaHang @Store_id = ?, @Ten_san_pham = ?, @Ten_danh_muc = ?, @Gia_tu = ?, @Gia_den = ?, @Sap_xep = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,
                storeId,
                tenSanPham,
                tenDanhMuc,
                giaTu,
                giaDen,
                sapXep
        );

        return rows;
    }
}
