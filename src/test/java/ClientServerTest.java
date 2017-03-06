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

        int messages = 0;
        Client c1 = new Client("localhost", "A");
        Client c2 = new Client("localhost", "B");
        messages += 2;

        c1.send("Hello!");
        messages++;
        pause();

        assertEquals(messages, c1.getReceivedLines());

        c2.send("Hi, B!");
        messages++;
        pause();

        assertEquals(messages, c2.getReceivedLines());
        assertEquals(messages, c1.getReceivedLines());

        for (int i = 1; i <= 10; i++) c1.send("Hello #" + i);
        messages += 10;
        pause();

        assertEquals(messages, c2.getReceivedLines());
        assertEquals(messages, c1.getReceivedLines());

        Client c3 = new Client("localhost", "New person");
        messages++;
        for (int i = 1; i <= 10; i++) c2.send("c3 message #" + i);
        messages += 10;
        pause();

        assertEquals(messages, c1.getReceivedLines());
        assertEquals(messages, c2.getReceivedLines());
        assertEquals(messages, c3.getReceivedLines());

        serverThread.interrupt();
    }

    private void pause() throws InterruptedException {
        Thread.sleep(100);
    }
}
