package eastin.Survive.logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ebricco on 8/8/18.
 *
 *
 */
public class Logger {

    //system constants
    static final int ALL = -1;
    static final int NONE = -2;

    static final List<LogHandler> defaultHandlers = new ArrayList<>(Arrays.asList(new SimpleConsoleLogHandler()));

    static int systemLogLevel = ALL;

    private List<LogHandler> handlers;
    private int defaultLoggerLogLevel;
    private MessageModifier mm;

    List<Integer> systemLevels;

    public Logger(int loggerLevel, List<LogHandler> handlers) {
        this(loggerLevel);

        this.handlers = handlers;
    }

    public Logger(int loggerLogLevel) {
        this.defaultLoggerLogLevel = loggerLogLevel;
        this.handlers = defaultHandlers;

        systemLevels = decompose(systemLogLevel, true);

        mm = null;
    }

    public Logger() {
        this(0);
    }

    public Logger setModifier(MessageModifier mm) {
        this.mm = mm;
        return this;
    }

    public void log(String message) {
        log(defaultLoggerLogLevel, message);
    }

    public void log(int logLevel, String message) {
        if(mm != null) {
            message = mm.modify(message);
        }
        if(shouldLog(logLevel)) {
            for(LogHandler handler:handlers) {
                handler.handle(message);
            }
        }
    }

    public void perform(int logLevel, Runnable r) {
        if(shouldLog(logLevel)) {
            r.run();
        }
    }

    //checks if any system number is in any logLevel number
    public boolean shouldLog(int logLevel) {

        if(systemLogLevel == ALL) {
            return true;
        } else if(systemLogLevel == NONE) {
            return false;
        }

        List<Integer> logLevels = decompose(logLevel, false);

        if(logLevels == null) {
            return false;
        }

        for(Integer i:systemLevels) {
            if(logLevels.contains(i)) {
                return true;
            }
        }

        return false;
    }

    public List<Integer> decompose(int num, boolean includeZero) {

        if(num < 0) {
            return null;
        }

        ArrayList<Integer> nums = new ArrayList<>();

        if(includeZero || num == 0) {
            nums.add(0);
        }

        int factor = getBiggestFactorTwoUnder(num);

        while(num >= 0 && factor >= 1) {
            if(num >= factor) {
                nums.add(factor);
                num -= factor;
            }

            factor /= 2;
        }

        return nums;
    }

    public int getBiggestFactorTwoUnder(int num) {
        int factor = 1;

        while(factor <= num) {
            factor *= 2;
        }

        factor /= 2;

        return factor;
    }

    public static void setSystemLogLevel(int level) {
        systemLogLevel = level;
    }

    public int getDefaultLoggerLogLevel() {
        return defaultLoggerLogLevel;
    }
}
