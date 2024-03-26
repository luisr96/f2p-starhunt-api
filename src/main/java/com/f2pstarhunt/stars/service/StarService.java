package com.f2pstarhunt.stars.service;

import com.f2pstarhunt.stars.model.Star;
import com.f2pstarhunt.stars.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StarService {

    private final StarRepository starRepository;

    public List<Star> getLiveStars() {
        return starRepository.findLiveStarsSince(Instant.now());
    }
}