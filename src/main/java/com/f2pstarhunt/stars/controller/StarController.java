package com.f2pstarhunt.stars.controller;

import com.f2pstarhunt.stars.model.api.StarDto;
import com.f2pstarhunt.stars.model.shared.StarLocation;
import com.f2pstarhunt.stars.model.shared.StarTier;
import com.f2pstarhunt.stars.service.StarService;
import com.f2pstarhunt.stars.service.StarUpdateResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/stars")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class StarController {

    // TODO check whether Spring's physical naming strategy and implicit naming strategy are correct.

    private final StarService starService;

    /**
     * Get all live stars.
     * @return the live stars
     */
    @GetMapping
    public List<StarDto> getLiveStars() {
        return starService.getLiveStars();
    }

    /**
     * Receive star information.
     * @param star the star
     * @return the id of the star
     */
    @PostMapping("/send")
    public long sendStar(StarDto star) {
        //TODO rate-limit

        log.info("Received star: " + star);

        return starService.send(star);
    }

    /**
     * Updates the star size
     * @param id the id of the star
     * @param size the new size
     */
    @PatchMapping("/{id}/update/{size}")
    public ResponseEntity<Void> updateStar(@PathVariable long id, @PathVariable int size) {
        //TODO rate-limit

        StarUpdateResult updateResult = starService.update(id, StarTier.bySize(size));
        return updateResultToResponse(updateResult);
    }

    /**
     * Updates the star size
     * @param world the world of the star
     * @param location the location of the star
     * @param size the new size
     */
    @PatchMapping("/{world}/{location}/update/{size}")
    public ResponseEntity<Void> updateStar(@PathVariable int world, @PathVariable StarLocation location, @PathVariable int size) {
        //TODO rate-limit

        StarUpdateResult updateResult = starService.update(world, location, StarTier.bySize(size));
        return updateResultToResponse(updateResult);
    }

    @DeleteMapping("{id}/poof/")
    public void poofStar(@PathVariable long id) {
        //TODO rate-limit

        starService.poof(id);
    }

    @DeleteMapping("{world}/{location}/poof/")
    public void poofStar(@PathVariable int world, @PathVariable StarLocation location) {
        //TODO rate-limit

        starService.poof(world, location);
    }

    @DeleteMapping("{id}/deplete")
    public void depleteStar(@PathVariable long id) {
        //TODO rate-limit

        starService.deplete(id);
    }

    @DeleteMapping("{world}/{location}/deplete/")
    public void depleteStar(@PathVariable int world, @PathVariable StarLocation location) {
        //TODO rate-limit

        starService.deplete(world, location);
    }

    /**
     * Makes the star publicly visible.
     * @param id the id of the star
     */
    @PatchMapping("/{id}/publish")
    public void publishStar(@PathVariable long id) {
        starService.publish(id);
    }

    private static ResponseEntity<Void> updateResultToResponse(StarUpdateResult result) {
        return switch (result) {
            case UPDATED -> ResponseEntity.noContent().build();
            case ALREADY_UPDATED -> ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            case NOT_FOUND -> ResponseEntity.notFound().build();
        };
    }
}