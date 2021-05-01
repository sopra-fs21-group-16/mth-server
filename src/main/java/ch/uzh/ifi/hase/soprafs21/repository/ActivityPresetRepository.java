package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("activityPresetRepository")
public interface ActivityPresetRepository extends JpaRepository<ActivityPreset, Long>{
    ActivityPreset findById(long id);
    List<ActivityPreset> findByActivityCategory(ActivityCategory activityCategory);
}
