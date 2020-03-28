package com.mostimes.haitao.product.service;

import com.mostimes.haitao.entity.PmsProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomeProductService {
    List<PmsProduct> getRecommendProduct(String productType);
}
