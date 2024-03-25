package com.f2pstarhunt.stars.service;

import com.f2pstarhunt.stars.model.Star;
import com.f2pstarhunt.stars.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StarService {

    private final StarRepository starRepository;

    @Autowired
    public StarService(StarRepository starRepository) {
        this.starRepository = starRepository;
    }

    public List<Star> getAllStars() {
        return starRepository.findAll();
    }
}