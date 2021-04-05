package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("activityRepository")
public interface ActivityRepository extends JpaRepository<Activity, Long>{
    Activity findById(long id);
}
