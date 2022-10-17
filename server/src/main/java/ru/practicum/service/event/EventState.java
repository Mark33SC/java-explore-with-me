package ru.practicum.service.event;

public enum EventState {

    PENDING("PENDING"),

    PUBLISHED("PUBLISHED"),

    CANCELED("CANCELED");

    private final String val;

    EventState(String val) {
        this.val = val;
    }

    public static EventState findByName(String name) {
        for (EventState state : values()) {
            if (state.name().equalsIgnoreCase(name)) {
                return state;
            }
        }

        throw new IllegalArgumentException(String.format("EventState with name:%s not exist", name));
    }

    public String getVal() {
        return val;
    }
}
