package za.co.wethinkcode.robots;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.server.MultiServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

class MultiServerTest {
    @Test
    void testFullServerFlow() throws IOException, InterruptedException {
        int testPort = 5002; // Use a different port than production

        // Start server in background
        Thread serverThread = new Thread(() -> {
            try {
                MultiServer.main(new String[]{String.valueOf(testPort)});
            } catch (Exception ignored) {}
        });
        serverThread.start();

        // Give it a moment to boot
        Thread.sleep(200);

        try (Socket client = new Socket("localhost", testPort);
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

            // Act: Send a command (assuming your handler responds to "launch")
            out.println("launch hal");

            // Assert: Check if the server responded
            String response = in.readLine();
            assertNotNull(response, "Server should send a response");
            assertTrue(response.contains("OK"), "Response should indicate success");
        } finally {
            serverThread.interrupt(); // Attempt to cleanup
        }
    }
}
