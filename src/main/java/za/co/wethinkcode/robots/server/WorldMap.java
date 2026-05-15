package za.co.wethinkcode.robots.server;


import com.fasterxml.jackson.annotation.JsonProperty;
import za.co.wethinkcode.robots.protocols.Obstacle;

import java.util.List;


/**
 * An immutable record representing a map.
 * Contains the minimum world dimensions required to use the map
 * and a list of {@link Obstacle}s to be placed in a world
 * <br>
 * Deserialized from a JSON map file using Jackson.
 * Minimum width and height must both be at least 1.
 */
public record WorldMap(
        @JsonProperty("minWidth")
        int minWidth,
        @JsonProperty("minHeight")
        int minHeight,
        @JsonProperty("obstacles")
        List<Obstacle> obstacles)
{
    /**
     * @throws IllegalArgumentException if minWidth or minHeight is less than 1
     */
    public WorldMap{
        validateSize(minWidth, minHeight);
    }

    private void validateSize(int width, int height){
        if (width < 1 || height < 1){
            throw new IllegalArgumentException("Invalid size");
        }
    }
}
