package za.co.wethinkcode.robots.protocols.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Config(
        @JsonProperty("width")
        int width,

        @JsonProperty("height")
        int height,

        @JsonProperty("visibility")
        int visibility,

        @JsonProperty("shieldMax")
        int shieldMax,

        @JsonProperty("reloadTime")
        int reloadTime,

        @JsonProperty("repairTime")
        int repairTime

) {
    public Config{
        validateSize(width, height);
        validateVisibility(visibility);
        validateTime(reloadTime);
        validateTime(repairTime);
    }
    private void validateSize(int width, int height){
        if (width < 0 || height < 0){
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









//package za.co.wethinkcode.robots.protocols;
//
//public record Config(
//        int width,
//        int height,
//        int visibility,
//        int shieldMax,
//        int reloadTime,
//        int repairTime
//) {
//    public Config{
//        validateSize(width, height);
//        validateVisibility(visibility);
//        validateTime(reloadTime);
//        validateTime(repairTime);
//    }
//    private void validateSize(int width, int height){
//        if (width < 0 || height < 0){
//            throw new IllegalArgumentException("Invalid world size");
//        }
//    }
//
//    private void validateVisibility(int visibility){
//        if (visibility <= 0){
//            throw new IllegalArgumentException("visibility can't be less than 1");
//        }
//    }
//
//    private void validateTime(int time){
//        if (time < 0){
//            throw new IllegalArgumentException("Time can't be negative");
//        }
//    }
//
//
//}
