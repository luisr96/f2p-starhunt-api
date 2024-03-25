package com.f2pstarhunt.stars.controller;

import com.f2pstarhunt.stars.model.Star;
import com.f2pstarhunt.stars.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stars")
public class StarController {

    private final StarService starService;

    @Autowired
    public StarController(StarService starService) {
        this.starService = starService;
    }

    @GetMapping
    public List<Star> getAllStars() {
        return starService.getAllStars();
    }
}