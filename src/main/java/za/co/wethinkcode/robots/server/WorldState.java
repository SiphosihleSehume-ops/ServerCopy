package za.co.wethinkcode.robots.server;

public record WorldState(
        int[] position,
        int visibility,
        int reload,
        int repair,
        int shields
) {
}
