package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userSwipeStatusRepository")
public interface UserSwipeStatusRepository extends JpaRepository<UserSwipeStatus, Long> {
    UserSwipeStatus findById(long id);
}