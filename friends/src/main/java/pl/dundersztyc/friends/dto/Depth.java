package pl.dundersztyc.friends.dto;

public record Depth(int value) {
    private static final int MIN_DEPTH = 1;
    private static final int MAX_DEPTH = 10;

    public Depth(int value) {
        if (value < MIN_DEPTH || value > MAX_DEPTH) {
            throw new IllegalArgumentException(String.format("depth must be in range [%d;%d]", MIN_DEPTH, MAX_DEPTH));
        }
        this.value = value;
    }
}
