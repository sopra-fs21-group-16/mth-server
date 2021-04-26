package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entities.SchedulingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("schedulingSessionRepository")
public interface SchedulingSessionRepository extends JpaRepository<SchedulingSession, Long> {
    SchedulingSession findById(long id);
}
