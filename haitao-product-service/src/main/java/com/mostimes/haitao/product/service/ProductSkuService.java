package com.mostimes.haitao.product.service;

import com.mostimes.haitao.entity.PmsSkuInfo;
import org.springframework.stereotype.Service;

@Service
public interface ProductSkuService {
    PmsSkuInfo getProductSku(String saleAttrIdList);
}
