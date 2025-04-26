package com.helb.helbhotel.model;

public enum AssignmentMode {
    RANDOM_ASSIGNMENT("Random Assignment"),
    QUIET_ZONE("Quiet Zone"),
    STAY_PURPOSE("Stay Purpose"),
    SEQUENTIAL_ASSIGNMENT("Sequential Assignment");

    private final String label;

    AssignmentMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Optional: Find mode by label
    public static AssignmentMode fromLabel(String label) {
        for (AssignmentMode mode : values()) {
            if (mode.getLabel().equals(label)) {
                return mode;
            }
        }
        return null; // or throw exception if needed
    }
}