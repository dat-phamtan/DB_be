package com.example.database_assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException; // Import mới
import org.springframework.http.HttpStatus; // Import mới
import org.springframework.http.ResponseEntity; // Import mới
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductSearchController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    // --- HÀM ĐÃ ĐƯỢC SỬA LẠI ---
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam Integer storeId,
            @RequestParam(required = false) String tenSanPham,
            @RequestParam(required = false) String tenDanhMuc,
            @RequestParam(required = false) BigDecimal giaTu,
            @RequestParam(required = false) BigDecimal giaDen,
            @RequestParam(required = false, defaultValue = "Ngay_dang_DESC") String sapXep
    ) {
        String sql = "EXEC sp_TimKiemSanPham_CuaHang @Store_id = ?, @Ten_san_pham = ?, @Ten_danh_muc = ?, @Gia_tu = ?, @Gia_den = ?, @Sap_xep = ?";

        try {
            // Thực thi câu lệnh
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,
                    storeId,
                    tenSanPham,
                    tenDanhMuc,
                    giaTu,
                    giaDen,
                    sapXep
            );
            // Thành công: Trả về HTTP 200 và danh sách kết quả
            return ResponseEntity.ok(rows);

        } catch (DataAccessException e) {
            // Lỗi SQL (RAISERROR/THROW): Lấy nội dung lỗi gốc
            String sqlErrorMessage = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();

            // Trả về HTTP 400 (Bad Request) để frontend nhận diện
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", sqlErrorMessage);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Các lỗi hệ thống khác: Trả về HTTP 500
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi hệ thống: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // --- HÀM BÊN DƯỚI GIỮ NGUYÊN ---
    // GET /api/products/{id}/detail?storeId=...
    @GetMapping("/{id}/detail")
    public Map<String, Object> detail(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer storeId
    ) throws Exception {
        String call = "{call sp_XemChiTiet_SanPham(?, ?)}";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(call)) {

            cs.setInt(1, id);
            if (storeId != null) cs.setInt(2, storeId);
            else cs.setNull(2, java.sql.Types.INTEGER);

            boolean hasResults = cs.execute();

            Map<String, Object> result = new HashMap<>();

            // The stored procedure returns multiple result sets in order:
            // 1: product basic info, 2: images, 3: variants, 4: categories, 5: reviews
            int resultSetIndex = 0;
            while (hasResults) {
                try (ResultSet rs = cs.getResultSet()) {
                    List<Map<String, Object>> rows = new ArrayList<>();
                    ResultSetMetaData md = rs.getMetaData();
                    int cols = md.getColumnCount();
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= cols; i++) {
                            String colName = md.getColumnLabel(i);
                            Object value = rs.getObject(i);
                            row.put(colName, value);
                        }
                        rows.add(row);
                    }

                    if (resultSetIndex == 0) result.put("product", rows.isEmpty() ? null : rows.get(0));
                    else if (resultSetIndex == 1) result.put("images", rows);
                    else if (resultSetIndex == 2) result.put("variants", rows);
                    else if (resultSetIndex == 3) result.put("categories", rows);
                    else if (resultSetIndex == 4) result.put("reviews", rows);
                    else result.put("rs_" + resultSetIndex, rows);
                }

                resultSetIndex++;
                hasResults = cs.getMoreResults();
            }

            return result;
        }
    }
}