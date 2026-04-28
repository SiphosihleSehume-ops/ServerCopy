package za.co.wethinkcode.robots;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.protocols.commands.Command;
import za.co.wethinkcode.robots.robot.Robot;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;


import za.co.wethinkcode.robots.robot.RobotType;
import za.co.wethinkcode.robots.server.ClientHandler;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientHandlerTest {

    @Test
    void testClientHandlerLogic() throws IOException {
        // 1. Arrange: Create real dependencies
        // Assuming RobotType is an Enum (e.g., RobotType.NORMAL)
        Robot testRobot = new Robot("HAL", RobotType.SHOOTER);
        Command commandProcessor = new Command();

        // 2. Mock the Socket using mockito-core
        Socket mockSocket = mock(Socket.class);

        // We add "quit" or a newline to ensure the handler doesn't loop forever
        String simulatedInput = "look\nquit\n";
        ByteArrayInputStream input = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Instruct the mock socket what to return when the handler calls these methods
        when(mockSocket.getInputStream()).thenReturn(input);
        when(mockSocket.getOutputStream()).thenReturn(output);

        // 3. Act: Run the handler
        // Note: Renamed from 'ClientHandler' to 'ClientHandlerTest' to avoid naming conflicts
        ClientHandler handler = new ClientHandler(mockSocket, commandProcessor, testRobot);

        try {
            handler.run();
        } catch (Exception e) {
            // If your run() method throws an EOFException or similar when 'quit' happens
        }

        // 4. Assert: Check if the output contains the Robot's name or state strings
        String actualOutput = output.toString();

        // Verify that the handler actually processed the "look" command
        // by checking for data returned by the Robot.state() method
        assertTrue(actualOutput.contains("HAL"), "Output should contain robot name 'HAL'");
        assertTrue(actualOutput.contains("position"), "Output should contain 'position' key from state map");
    }
}





//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class ClientHandler {
//    @Test
//    void testClientHandlerLogic() throws IOException {
//        // 1. Arrange: Create real dependencies
//        Robot testRobot = new Robot();
//        Command commandProcessor = new Command();
//
//        // 2. Mock the Socket to simulate a client sending "look"
//        Socket mockSocket = mock(Socket.class);
//        ByteArrayInputStream input = new ByteArrayInputStream("look\n".getBytes());
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//        when(mockSocket.getInputStream()).thenReturn(input);
//        when(mockSocket.getOutputStream()).thenReturn(output);
//
//        // 3. Act: Run the handler logic once
//        ClientHandler handler = new ClientHandler(mockSocket, commandProcessor, testRobot);
//        handler.run(); // Run manually in the test thread
//
//        // 4. Assert: Did the robot "see" anything?
//        assertTrue(output.toString().contains("Robot"), "Handler should return robot status");
//    }
//}
