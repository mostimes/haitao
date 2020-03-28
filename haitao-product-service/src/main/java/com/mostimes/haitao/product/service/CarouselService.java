package com.mostimes.haitao.product.service;

import com.mostimes.haitao.entity.CmsCarousel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarouselService {
    List<CmsCarousel> getCarousel(String type);
}
