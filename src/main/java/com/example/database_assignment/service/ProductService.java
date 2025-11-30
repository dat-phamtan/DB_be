package com.example.database_assignment.service;

import com.example.database_assignment.dto.ProductRequest;
import com.example.database_assignment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    //them
    public void createProduct(ProductRequest request){
        productRepository.callInsertProduct(
                request.getStoreId(),
                request.getTenSanPham(),
                request.getMoTaChiTiet(),
                request.getTinhTrang(),
                request.getTrongLuong()
        );
    }

    //sua
    public void updateProduct(Integer productId, ProductRequest request){
        productRepository.callUpdateProduct(
                productId,
                request.getTenSanPham(),
                request.getMoTaChiTiet(),
                request.getTinhTrang(),
                request.getTrongLuong(),
                request.getTrangThaiDang()
        );
    }

    //xoa
    public void deleteProduct(Integer productId){
        productRepository.callDeleteProduct(
                productId
        );
    }
}
