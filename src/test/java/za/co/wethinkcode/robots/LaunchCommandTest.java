package za.co.wethinkcode.robots;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.protocols.Position;
import za.co.wethinkcode.robots.protocols.commands.LaunchCommand;


import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.server.World;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LaunchCommandTest {

    private World mockWorld;
    private Robot mockRobot;
    private LaunchCommand launchCommand;

    @BeforeEach
    void setUp() {
        mockWorld = mock(World.class);
        mockRobot = mock(Robot.class);
        launchCommand = new LaunchCommand(List.of("Bender"));
    }

    @Test
    void testExecuteLaunchSuccess() {
        // Setup: World is not full and has visibility 10
        when(mockWorld.isFull()).thenReturn(false);
        when(mockWorld.getVisibility()).thenReturn(10);

        // Mock Robot position and state
        Position fixedPos = new Position(5, 4);
        when(mockRobot.getCurrentPosition()).thenReturn(fixedPos);
        when(mockRobot.state()).thenReturn(Map.of("status", "NORMAL"));

        // Execute
        Response response = launchCommand.execute(mockRobot, mockWorld);

        // Verify: World's addRobot was called once
        verify(mockWorld, times(1)).addRobot(eq(mockRobot), any(Position.class));

        // Assert: Check response structure
        assertEquals("OK", response.getResult());
        Map<String, Object> data = (Map<String, Object>) response.getStatus();
        assertEquals("5, 4", data.get("position").toString());
        assertEquals(10, data.get("visibility"));
    }

    @Test
    void testExecuteLaunchWorldFull() {
        // Setup: World is full
        when(mockWorld.isFull()).thenReturn(true);

        // Execute
        Response response = launchCommand.execute(mockRobot, mockWorld);

        // Verify: addRobot was NEVER called
        verify(mockWorld, never()).addRobot(any(), any());

        // Assert: Check for error response
        assertEquals("ERROR", response.getResult());
        assertEquals("No more space in this world", response.getResult());
    }
}



//class MultiServerTest {
//    @Test
//    void testFullServerFlow() throws IOException, InterruptedException {
//        int testPort = 5002; // Use a different port than production
//
//        // Start server in background
//        Thread serverThread = new Thread(() -> {
//            try {
//                MultiServer.main(new String[]{String.valueOf(testPort)});
//            } catch (Exception ignored) {}
//        });
//        serverThread.start();
//
//        // Give it a moment to boot
//        Thread.sleep(200);
//
//        try (Socket client = new Socket("localhost", testPort);
//             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
//
//            // Act: Send a command (assuming your handler responds to "launch")
//            out.println("launch hal");
//
//            // Assert: Check if the server responded
//            String response = in.readLine();
//            assertNotNull(response, "Server should send a response");
//            assertTrue(response.contains("OK"), "Response should indicate success");
//        } finally {
//            serverThread.interrupt(); // Attempt to cleanup
//        }
//    }
//}
