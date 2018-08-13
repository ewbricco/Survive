package eastin.Survive.logging;

/**
 * Created by ebricco on 8/8/18.
 */
public class SimpleFileAppender implements LogHandler {

    private boolean printTime;

    public SimpleFileAppender() {
        printTime = false;
    }

    public SimpleFileAppender(boolean printTime) {
        this.printTime = printTime;
    }

    public void handle(String message) {

    }
}
