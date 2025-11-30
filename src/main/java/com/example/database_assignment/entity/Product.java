package com.example.database_assignment.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data //lombok tuự sinh getter setter
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //tự tăng id
    @Column(name = "Product_id")
    private Integer productId;

    @Column(name = "Store_id")
    private Integer storeId;

    @Column(name = "Ten_san_pham", nullable = false, length = 200)
    private String tenSanPham;

    @Column(name = "Mo_ta_chi_tiet", columnDefinition = "nvarchar(max)")
    private String moTaChiTiet;

    @Column(name = "Tinh_trang", length = 20)
    private String tinhTrang;

    // Trong SQL là decimal(10,2) -> Java nên dùng BigDecimal để chính xác
    @Column(name = "Trong_luong")
    private BigDecimal trongLuong;

    @Column(name = "Trang_thai_dang", length = 20)
    private String trangThaiDang;

    //sql (datetime) --> java (LocalDateTime)
    @Column(name = "Ngay_dang")
    private LocalDateTime ngayDang;
}