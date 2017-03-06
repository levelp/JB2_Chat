import chat.Client;
import chat.Server;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class ClientServerTest extends Assert {
    @Before
    public void setUp() throws Exception {
        File file = new File("test.txt");
        if (file.exists())
            assertTrue(file.delete());
    }

    @Test
    public void testClientServer() throws Exception {
        Thread serverThread = new Thread(() -> {
            try {
                new Server("test");
            } catch (Exception e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        });
        serverThread.start();

        Client c1 = new Client("localhost", "A");
        Client c2 = new Client("localhost", "B");

        c1.send("Hello!");
        Thread.sleep(100);
        assertEquals(1, c1.getLinesCounter());

        c2.send("Hi, B!");

        Thread.sleep(100);
        assertEquals(2, c2.getLinesCounter());
        assertEquals(2, c1.getLinesCounter());

        for (int i = 1; i <= 10; i++) {
            c1.send("Hello #" + i);
        }

        Thread.sleep(100);
        assertEquals(12, c2.getLinesCounter());
        assertEquals(12, c1.getLinesCounter());

        serverThread.interrupt();
        new File("test.txt").delete();
    }
}
