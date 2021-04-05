package ch.uzh.ifi.hase.soprafs21.ExternalAPI;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Google Point of Interest --> used to find locations for activities
 */
@Entity
@Table(name = "GooglePOI")
public class GooglePOI implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String keyword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
