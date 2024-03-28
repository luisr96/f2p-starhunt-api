package com.f2pstarhunt.stars.service;

import com.f2pstarhunt.stars.model.api.StarDto;
import com.f2pstarhunt.stars.model.db.Star;
import com.f2pstarhunt.stars.model.db.StarStatus;
import com.f2pstarhunt.stars.model.shared.StarLocation;
import com.f2pstarhunt.stars.model.shared.StarTier;
import com.f2pstarhunt.stars.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StarService {

    private final StarRepository starRepository;

    public List<StarDto> getLiveStars() {
        List<Star> stars = starRepository.findActiveStars();
        return stars.stream().map(StarService::toDto).toList();
    }

    @Transactional
    public long send(StarDto dto) {
        Star star = starRepository.findAliveStar(dto.world(), dto.location());
        if (star == null) {
            star = starRepository.save(newStar(dto));
        }
        return star.getId();
    }

    @Transactional
    public StarUpdateResult update(long id, StarTier tier) {
        return update(starRepository.findActiveById(id), tier);
    }

    @Transactional
    public StarUpdateResult update(int world, StarLocation location, StarTier tier) {
        return update(starRepository.findActiveByLocation(world, location), tier);
    }

    @Transactional
    public StarDeleteResult poof(long id) {
        return delete(starRepository.findActiveById(id), StarStatus.DISINTEGRATED);
    }

    @Transactional
    public StarDeleteResult poof(int world, StarLocation location) {
        return delete(starRepository.findActiveByLocation(world, location), StarStatus.DISINTEGRATED);
    }

    @Transactional
    public StarDeleteResult deplete(long id) {
        return delete(starRepository.findActiveById(id), StarStatus.DEPLETED);
    }

    @Transactional
    public StarDeleteResult deplete(int world, StarLocation location) {
        return delete(starRepository.findActiveByLocation(world, location), StarStatus.DEPLETED);
    }

    public void publish(long id) {
        Star star = starRepository.findAliveStar(id);
        if (star != null && !star.isVisible()) {
            //mark it as visible and save
            star.setVisible(true);
            starRepository.save(star);
        }
    }

    private StarDeleteResult delete(Star star, StarStatus deleteStatus) {
        if (!isDeletedStatus(deleteStatus))
            throw new IllegalArgumentException("Not a delete status: " + deleteStatus);

        if (star != null) {
            if (star.isActive()) {
                //star was active, but not any longer
                star.setTier(null);
                star.setStatus(deleteStatus);
                star.setDisappearedAt(Instant.now());
                starRepository.save(star);
                return StarDeleteResult.DELETED;
            } else {
                //star was already marked not alive
                return StarDeleteResult.ALREADY_DELETED;
            }
        } else {
            //client thinks the star exists, but server does not
            return StarDeleteResult.NOT_FOUND;
        }
    }

    private StarUpdateResult update(Star star, StarTier newTier) {
        if (star != null) {
            if (star.getTier().isGreaterThan(newTier)) {
                //tier degrades
                star.setTier(newTier);
                starRepository.save(star);
                return StarUpdateResult.UPDATED;
            } else {
                //no update required
                return StarUpdateResult.ALREADY_UPDATED;
            }
        } else {
            //client thinks the star exists, but server does not
            return StarUpdateResult.NOT_FOUND;
        }
    }

    private static boolean isDeletedStatus(StarStatus status) {
        return switch (status) {
            case DEPLETED, DISINTEGRATED -> true;
            default -> false;
        };
    }

    private static Star newStar(StarDto dto) {
        if (dto.tier() == null)
            throw new IllegalArgumentException("new star without tier: " + dto);

        return new Star(
                null,
                dto.world(),
                dto.location(),
                dto.tier(),
                dto.discoveredBy(),
                dto.detectedAt(),
                false,
                null,
                StarStatus.ALIVE
        );
    }

    private static StarDto toDto(Star star) {
        return new StarDto(
                star.getId(),
                star.getWorld(),
                star.getLocation(),
                star.getTier(),
                star.getDiscoveredBy(),
                star.getDetectedAt()
        );
    }

}