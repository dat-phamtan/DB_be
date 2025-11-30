package com.example.database_assignment.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    private Integer productId;
    private String tenSanPham;
    private String moTaChiTiet;
    private String tinhTrang;
    private BigDecimal trongLuong;
    private String trangThaiDang;
    private Integer storeId;
}