package eastin.Survive.logging;

/**
 * Created by ebricco on 8/8/18.
 */
public class SimpleConsoleLogHandler implements LogHandler {
    public void handle(String message) {
        System.out.println(message);
    }
}
