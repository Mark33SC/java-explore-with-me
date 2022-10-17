package ru.practicum.service.request;

public enum RequestStatus {

    PENDING("PENDING"),

    CONFIRMED("CONFIRMED"),

    REJECTED("REJECTED"),

    CANCELED("CANCELED");

    private final String val;

    RequestStatus(String val) {
        this.val = val;
    }

    public static RequestStatus findByName(String name) {
        for (RequestStatus status : values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return status;
            }
        }

        throw new IllegalArgumentException(String.format("RequestStatus with name:%s not exist", name));
    }

    public String getVal() {
        return val;
    }
}
