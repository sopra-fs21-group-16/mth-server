package ch.uzh.ifi.hase.soprafs21.constant;

public enum ActivityCategory {
    COOKING("cooking-pot"),
    EATING("hamburger"),
    THEATRE("comedy"),
    MOVIES("movie-projector"),
    MUSIC("music"),
    MUSEUMS("bust"),
    OUTDOOR_ACTIVITY("campfire"),
    SIGHTSEEING("camera"),
    TRAVEL("airport"),
    WELLNESS("spa-flower"),
    SHOPPING("shopping-bag"),
    GAMES("game-controller"),
    SPORTS("track-and-field"),
    WINTERSPORTS("skiing");

    public final String icon;

    ActivityCategory(String icon) {
        this.icon = icon;
    }

    public String toString() {
        return this.icon;
    }
}