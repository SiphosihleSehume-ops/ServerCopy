package za.co.wethinkcode.robots.server;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;

/**
 * Utility class for loading world maps from JSON files
 * in the user's home directory.
 */
public class MapLoader {
    private static final ObjectMapper MAPPER =
            new ObjectMapper();

    private MapLoader(){
        throw new UnsupportedOperationException("Utility Class");
    }

    /**
     * Loads a {@link WorldMap} by name from {@code ~/.toyrobots/maps/<mapName>.json}.
     * Extracts bundled maps to that directory first if they are missing.
     * @param mapName the name of the map, without the {@code .json} extension
     * @return an instance of {@link WorldMap}
     * @throws IOException if the map cannot be found or read
     */
    public static WorldMap load(String mapName) throws IOException{
        Path path = Paths.get(System.getProperty("user.home"),
                ".toyrobots/maps").resolve(mapName + ".json");
        return load(path);
    }

    /**
     * Loads a {@link WorldMap} from the given file path.
     * If the file does not exist, bundled maps are extracted to its parent directory first.
     * @param filePath the full path to the map JSON file
     * @return an instance of {@link WorldMap}
     * @throws IOException if the map cannot be found or if reading fails
     */
    public static WorldMap load(Path filePath) throws IOException {
        if (Files.exists(filePath) && Files.size(filePath) > 0){
            return MAPPER.readValue(filePath.toFile(), WorldMap.class);
        }

        createMaps(filePath);

        if (!Files.exists(filePath)){
            throw new IOException("Map not found\n" +
                    "Available maps: " + availableFiles(filePath));
        }
        return MAPPER.readValue(filePath.toFile(), WorldMap.class);
    }

    private static void createMaps(Path filePath) throws IOException{
        Path dir = filePath.getParent();

        if (dir != null){
            Files.createDirectories(dir);
        }

        InputStream stream = MapLoader.class
                .getResourceAsStream("/maps/maps.index");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream))
        )
        {
            String mapFileName;

            while ((mapFileName = reader.readLine()) != null){
                try (InputStream mapStream = MapLoader.class
                        .getResourceAsStream("/maps/" + mapFileName)) {

                    if (mapStream == null) {
                        continue;
                    }

                    Path output = dir.resolve(mapFileName);
                    Files.copy(mapStream, output,
                            StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }


    private static String availableFiles(Path filePath) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path dir = filePath.getParent();

        try (DirectoryStream<Path> files = Files.newDirectoryStream(dir)) {

            boolean first = true;

            for (Path file : files) {
                String name = file.getFileName().toString();
                int dot = name.lastIndexOf('.');

                if (dot > 0) {
                    name = name.substring(0, dot);
                }

                if (!first) {
                    fileNames.append(", ");
                }

                fileNames.append(name);
                first = false;
            }
        }

        return fileNames.toString();
    }

}

