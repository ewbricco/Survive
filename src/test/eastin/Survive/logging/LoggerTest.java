package eastin.Survive.logging;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by ebricco on 8/8/18.
 */
public class LoggerTest {

    @Test
    public void testDecompose() {
        Logger log = new Logger();

        assert(log.decompose(5, true).size() == 3);
        assert(log.decompose(5, true).contains(0));
        assert(log.decompose(5, true).contains(1));
        assert(log.decompose(5, true).contains(4));
        assert(log.decompose(3, true).size() == 3);
        assert(log.decompose(3, true).contains(1));
        assert(log.decompose(3, true).contains(2));
        assert(log.decompose(16, true).size() == 2);
        assert(log.decompose(15, true).size() == 5);
        assert(log.decompose(15, false).size() == 4);
    }

    @Test
    public void testPower2() {
        Logger log = new Logger();

        assert(log.getBiggestFactorTwoUnder(5) == 4);
        assert(log.getBiggestFactorTwoUnder(4) == 4);
        assert(log.getBiggestFactorTwoUnder(64) == 64);
        assert(log.getBiggestFactorTwoUnder(63) == 32);
    }

    @Test
    public void testShouldLog() {

        Logger.setSystemLogLevel(Logger.ALL);
        Logger log = new Logger();

        assert(log.shouldLog(log.getDefaultLoggerLogLevel()));
        assert(log.shouldLog(10000));
        assert(log.shouldLog(0));
        assert(log.shouldLog(32));
        assert(log.shouldLog(31));


        //16 + 8
        Logger.setSystemLogLevel(24);
        log = new Logger();

        assert(log.shouldLog(log.getDefaultLoggerLogLevel()));
        assert(!log.shouldLog(3));
        assert(!log.shouldLog(7));
        assert(!log.shouldLog(5));
        assert(!log.shouldLog(39));
        assert(log.shouldLog(0));
        assert(log.shouldLog(16));
        assert(log.shouldLog(8));
        assert(log.shouldLog(24));
        assert(log.shouldLog(10));
    }

    @Test
    public void simple() {
        Logger log = new Logger();
        log.log("hi"); //should print hi to console
    }

    @Test
    public void complex() {
        Logger log = new Logger(5, Arrays.asList((LogHandler) message -> System.out.println(message)))
                .setModifier(message -> System.currentTimeMillis() + ": " + message);

        log.log("hi"); //should log time and hi
    }


}
