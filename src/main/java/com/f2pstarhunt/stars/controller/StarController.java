package com.f2pstarhunt.stars.controller;

import com.f2pstarhunt.stars.model.Star;
import com.f2pstarhunt.stars.model.StarTier;
import com.f2pstarhunt.stars.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stars")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StarController {

    // TODO check whether Spring's physical naming strategy and implicit naming strategy are correct.

    private final StarService starService;

    /**
     * Get all live stars.
     * @return the live stars
     */
    @GetMapping
    public List<Star> getLiveStars() {
        return starService.getLiveStars();
    }

    /**
     * Receive star information.
     * @param star the star
     * @return the id of the star
     */
    @PostMapping("/send")
    public long sendStar(Star star) {
        //TODO rate-limit

        //TODO return id
        return starService.send(star);
    }

    @PatchMapping("/update/{id}/{size}")
    public void updateStar(@PathVariable long id, @PathVariable int size) {
        //TODO rate-limit

        return starService.update(id, StarTier.bySize(size));
    }

    @DeleteMapping("/poof/{id}")
    public void poofStar(@PathVariable long id) {
        //TODO rate-limit

        starService.poof(id);
    }

    @PatchMapping("/pubish/{id}")
    public void publishStar(@PathVariable long id) {
        starService.publish(id);
    }
}