package link.s.sls.access;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author sweeter
 * @date 2020/11/21
 */
public class AccessLogFilter extends Filter<ILoggingEvent> {
    /**
     * If the decision is <code>{@link FilterReply#DENY}</code>, then the event will be
     * dropped. If the decision is <code>{@link FilterReply#NEUTRAL}</code>, then the next
     * filter, if any, will be invoked. If the decision is
     * <code>{@link FilterReply#ACCEPT}</code> then the event will be logged without
     * consulting with other filters in the chain.
     *
     * @param event The event to decide upon.
     */
    @Override
    public FilterReply decide(ILoggingEvent event) {
        // 判断日志名是否包含access
        if (event.getLoggerName().contains("access")) {

            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}
