package me.miki.shindo.helpers.animation.normal;

public enum Direction {
    FORWARDS,
    BACKWARDS;

    public Direction opposite() {
        if (this == Direction.FORWARDS) {
            return Direction.BACKWARDS;
        } else {
        	return Direction.FORWARDS;
        }
    }
}