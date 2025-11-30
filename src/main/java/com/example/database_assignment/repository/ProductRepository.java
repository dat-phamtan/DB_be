package com.example.database_assignment.repository;

import com.example.database_assignment.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    //interface gọi thủ tục insert
    @Modifying
    @Transactional
    @Query(value = """
            EXEC Insert_Product
                @Store_id = :storeId,
                @Ten_san_pham = :tenSp,
                @Mo_ta_chi_tiet = :moTa,
                @Tinh_trang = :tinhTrang,
                @Trong_luong = :trongLuong
            """, nativeQuery = true)

    void callInsertProduct(
            @Param("storeId") Integer storeId,
            @Param("tenSp") String tenSp,
            @Param("moTa") String moTa,
            @Param("tinhTrang") String tinhTrang,
            @Param("trongLuong") BigDecimal trongLuong
    );

    //interface gọi thủ tục update
    @Modifying
    @Transactional
    @Query(value = """
            EXEC Update_Product
                @Product_id = :productId,
                @Ten_san_pham = :tenSp,
                @Mo_ta_chi_tiet = :moTa,
                @Tinh_trang = :tinhTrang,
                @Trong_luong = :trongLuong,
                @Trang_thai_dang = :trangThaiDang
            """, nativeQuery = true)

    void callUpdateProduct(
            @Param("productId") Integer productId,
            @Param("tenSp") String tenSp,
            @Param("moTa") String moTa,
            @Param("tinhTrang") String tinhTrang,
            @Param("trongLuong") BigDecimal trongLuong,
            @Param("trangThaiDang") String trangThaiDang
    );

    //interface gọi thủ tục delete
    @Modifying
    @Transactional
    @Query(value = """
            EXEC Delete_Product
                @Product_id = :productId
            """, nativeQuery = true)

    void callDeleteProduct(
            @Param("productId") Integer productId
    );
}
