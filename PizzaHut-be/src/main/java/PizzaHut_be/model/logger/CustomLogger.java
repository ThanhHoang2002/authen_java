package PizzaHut_be.model.logger;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

import java.util.Map;

public class CustomLogger implements Logger {
    private final String name;
    private final String module;
    private final Logger defaultLog;

    private void addConfig() {
        MDC.put("module", module);
        MDC.put("line", String.valueOf(this.getLineNumber()));
    }

    private void removeConfig() {
        MDC.remove("module");
        MDC.remove("line");
    }

    private Integer getLineNumber() {
        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            for (StackTraceElement stackTraceElement : entry.getValue()) {
                if (stackTraceElement.getClassName().equals(this.name)) {
                    return stackTraceElement.getLineNumber();
                }
            }
        }
        return null;
    }

    public CustomLogger(@Nonnull String name) {
        this.name = name;
        this.module = name.split("\\.")[2];
        this.defaultLog = LoggerFactory.getLogger(name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public LoggingEventBuilder makeLoggingEventBuilder(Level level) {
        return defaultLog.makeLoggingEventBuilder(level);
    }

    @Override
    public LoggingEventBuilder atLevel(Level level) {
        return defaultLog.atLevel(level);
    }

    @Override
    public boolean isEnabledForLevel(Level level) {
        return defaultLog.isEnabledForLevel(level);
    }

    @Override
    public boolean isTraceEnabled() {
        return defaultLog.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        addConfig();
        defaultLog.trace(msg);
        removeConfig();
    }

    @Override
    public void trace(String format, Object arg) {
        addConfig();
        defaultLog.trace(format, arg);
        removeConfig();
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.trace(format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void trace(String format, Object... arguments) {
        addConfig();
        defaultLog.trace(format, arguments);
        removeConfig();
    }

    @Override
    public void trace(String msg, Throwable t) {
        addConfig();
        defaultLog.trace(msg, t);
        removeConfig();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return defaultLog.isTraceEnabled(marker);
    }

    @Override
    public LoggingEventBuilder atTrace() {
        return defaultLog.atTrace();
    }

    @Override
    public void trace(Marker marker, String msg) {
        addConfig();
        defaultLog.trace(marker, msg);
        removeConfig();
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        addConfig();
        defaultLog.trace(marker, format, arg);
        removeConfig();
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.trace(marker, format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        addConfig();
        defaultLog.trace(marker, format, argArray);
        removeConfig();
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        addConfig();
        defaultLog.trace(marker, msg, t);
        removeConfig();
    }

    @Override
    public boolean isDebugEnabled() {
        return defaultLog.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        addConfig();
        defaultLog.debug(msg);
        removeConfig();
    }

    @Override
    public void debug(String format, Object arg) {
        addConfig();
        defaultLog.debug(format, arg);
        removeConfig();
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.debug(format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void debug(String format, Object... arguments) {
        addConfig();
        defaultLog.debug(format, arguments);
        removeConfig();
    }

    @Override
    public void debug(String msg, Throwable t) {
        addConfig();
        defaultLog.debug(msg, t);
        removeConfig();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return defaultLog.isDebugEnabled();
    }

    @Override
    public void debug(Marker marker, String msg) {
        addConfig();
        defaultLog.debug(marker, msg);
        removeConfig();
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        addConfig();
        defaultLog.debug(marker, format, arg);
        removeConfig();
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.debug(marker, format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        addConfig();
        defaultLog.debug(marker, format, arguments);
        removeConfig();
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        addConfig();
        defaultLog.debug(marker, msg, t);
        removeConfig();
    }

    @Override
    public LoggingEventBuilder atDebug() {
        return defaultLog.atDebug();
    }

    @Override
    public boolean isInfoEnabled() {
        return defaultLog.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        addConfig();
        defaultLog.info(msg);
        removeConfig();
    }

    @Override
    public void info(String format, Object arg) {
        addConfig();
        defaultLog.info(format, arg);
        removeConfig();
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.info(format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void info(String format, Object... arguments) {
        addConfig();
        defaultLog.info(format, arguments);
        removeConfig();
    }

    @Override
    public void info(String msg, Throwable t) {
        addConfig();
        defaultLog.info(msg, t);
        removeConfig();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return defaultLog.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        addConfig();
        defaultLog.info(marker, msg);
        removeConfig();
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        addConfig();
        defaultLog.info(marker, format, arg);
        removeConfig();
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.info(marker, format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        addConfig();
        defaultLog.info(marker, format, arguments);
        removeConfig();
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        addConfig();
        defaultLog.info(marker, msg, t);
        removeConfig();
    }

    @Override
    public LoggingEventBuilder atInfo() {
        return defaultLog.atInfo();
    }

    @Override
    public boolean isWarnEnabled() {
        return defaultLog.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        addConfig();
        defaultLog.warn(msg);
        removeConfig();
    }

    @Override
    public void warn(String format, Object arg) {
        addConfig();
        defaultLog.warn(format, arg);
        removeConfig();
    }

    @Override
    public void warn(String format, Object... arguments) {
        addConfig();
        defaultLog.warn(format, arguments);
        removeConfig();
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.warn(format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void warn(String msg, Throwable t) {
        addConfig();
        defaultLog.error(msg);
        removeConfig();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return defaultLog.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        addConfig();
        defaultLog.warn(marker, msg);
        removeConfig();
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        addConfig();
        defaultLog.warn(marker, format, arg);
        removeConfig();
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.warn(marker, format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        addConfig();
        defaultLog.warn(marker, format, arguments);
        removeConfig();
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        addConfig();
        defaultLog.warn(marker, msg, t);
        removeConfig();
    }

    @Override
    public LoggingEventBuilder atWarn() {
        return defaultLog.atWarn();
    }

    @Override
    public boolean isErrorEnabled() {
        return defaultLog.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        addConfig();
        defaultLog.error(msg);
        removeConfig();
    }

    @Override
    public void error(String format, Object arg) {
        addConfig();
        defaultLog.error(format, arg);
        removeConfig();
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.error(format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void error(String format, Object... arguments) {
        addConfig();
        defaultLog.error(format, arguments);
        removeConfig();
    }

    @Override
    public void error(String msg, Throwable t) {
        addConfig();
        defaultLog.error(msg + t.getMessage(), t);
        removeConfig();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return defaultLog.isDebugEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        addConfig();
        defaultLog.error(marker, msg);
        removeConfig();
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        addConfig();
        defaultLog.error(marker, format, arg);
        removeConfig();
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        addConfig();
        defaultLog.error(marker, format, arg1, arg2);
        removeConfig();
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        addConfig();
        defaultLog.error(marker, format, arguments);
        removeConfig();
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        addConfig();
        defaultLog.error(marker, msg + t.getMessage(), t);
        removeConfig();
    }

    @Override
    public LoggingEventBuilder atError() {
        return defaultLog.atError();
    }

}
