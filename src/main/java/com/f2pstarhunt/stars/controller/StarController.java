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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Endpoints for clients such as a RuneLite plugin, Discord bot or website.
 */
@RestController
@RequestMapping("/stars")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class StarController {

    private static final String RANK_HEADER = "F2P-StarHunt-Rank";

    private final StarService starService;

    @GetMapping
    public ResponseEntity<Object/*List<StarDto> or String*/> getAliveStars(
            @RequestHeader(name = RANK_HEADER, required = false) String rank,
            @RequestParam(name = "status", required = false) String activeOrBackup) {
        boolean includeBackups = "backup".equalsIgnoreCase(activeOrBackup);
        if (rank == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body("You do not have permission to access this resource.");
        }
        return ResponseEntity.ok(starService.getAliveStars(includeBackups));
    }

    /**
     * Receive star information.
     * @param star the star
     * @return the id of the star
     */
    @PostMapping
    public long sendStar(@RequestBody StarDto star) {
        //TODO rate-limit

        log.info("Received star: {}", star);

        return starService.send(star);
    }

    /**
     * Updates the star size.
     * @param id the id of the star
     * @param size the new size
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStar(@PathVariable long id, @RequestBody int size) {
        //TODO rate-limit

        StarUpdateResult updateResult = starService.update(id, StarTier.bySize(size));
        return updateResultToResponse(updateResult);
    }

    /**
     * Updates the star size.
     * @param world the world of the star
     * @param location the location of the star
     * @param size the new size
     */
    @PatchMapping("/{world}/{location}")
    public ResponseEntity<Void> updateStar(@PathVariable int world, @PathVariable StarLocation location, @RequestBody int size) {
        //TODO rate-limit

        StarUpdateResult updateResult = starService.update(world, location, StarTier.bySize(size));
        return updateResultToResponse(updateResult);
    }

    /**
     * Mark a star as disintegrated.
     * @param id the id of the star
     */
    @DeleteMapping("{id}/poof/")
    public void poofStar(@PathVariable long id) {
        //TODO rate-limit

        starService.poof(id);
    }

    /**
     * Mark a star as disintegrated.
     * @param world the star's world
     * @param location the star's location
     */
    @DeleteMapping("{world}/{location}/poof/")
    public void poofStar(@PathVariable int world, @PathVariable StarLocation location) {
        //TODO rate-limit

        starService.poof(world, location);
    }

    /**
     * Mark a star as depleted.
     * @param id the id of the star
     */
    @DeleteMapping("{id}/deplete")
    public void depleteStar(@PathVariable long id) {
        //TODO rate-limit

        starService.deplete(id);
    }

    /**
     * Mark a star as depleted.
     * @param world the star's world
     * @param location the star's location
     */
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
        //TODO rate-limit

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