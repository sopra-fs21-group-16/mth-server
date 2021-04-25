package ch.uzh.ifi.hase.soprafs21.constant;

/**
 * enum, such that SwipeStatus is extendable for the future (i.e. for functions like SUPERLIKE or BLACKLIST)
 */
public enum SwipeStatus {
    TRUE, // swiped right
    FALSE, // swiped left
    INITIAL // untouched
}
