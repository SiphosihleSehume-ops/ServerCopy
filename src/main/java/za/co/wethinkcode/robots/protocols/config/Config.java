package za.co.wethinkcode.robots.protocols.config;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Immutable configuration settings loaded from JSON.
 * All values are validated on construction.
 */
public record Config(
        @JsonProperty("width")
        int width,

        @JsonProperty("height")
        int height,

        @JsonProperty("visibility")
        int visibility,

        @JsonProperty("shieldMax")
        int shieldMax,

        @JsonProperty("nrObstacles")
        int nrObstacles,

        @JsonProperty("reloadTime")
        int reloadTime,

        @JsonProperty("repairTime")
        int repairTime

)
{
    /**
     * Creates a validated Config instance.
     *
     * @param width      the width dimension, must be at least 1
     * @param height     the height dimension, must be at least 1
     * @param visibility the visibility range, must be at least 1
     * @param shieldMax  the maximum shield value, must be non-negative
     * @param reloadTime the reload duration in seconds, must be non-negative
     * @param repairTime the repair duration in seconds, must be non-negative
     * @throws IllegalArgumentException if any value fails validation
     */
    public Config{
        validateSize(width, height);
        validateVisibility(visibility);
        validateTime(reloadTime);
        validateTime(repairTime);
    }
    private void validateSize(int width, int height){
        if (width < 1 || height < 1){
            throw new IllegalArgumentException("Invalid world size");
        }
    }

    private void validateVisibility(int visibility){
        if (visibility < 1){
            throw new IllegalArgumentException("visibility can't be less than 1");
        }
    }

    private void validateTime(int time){
        if (time < 0){
            throw new IllegalArgumentException("Time can't be negative");
        }
    }

}