package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entities.ScheduledActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("scheduledActivityRepository")
public interface ScheduledActivityRepository extends JpaRepository<ScheduledActivity, Long> {
    ScheduledActivity findById(long id);
}
