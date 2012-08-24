package org.avaje.metric.logback;

import org.avaje.metric.CounterMetric;
import org.avaje.metric.MetricManager;
import org.avaje.metric.MetricName;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * Appender that counts error, warning and info events that are sent to it.
 * <p>
 * Lower level events (DEBUG, TRACE) are deemed uninteresting.
 * </p>
 */
public final class LogMetricAppender extends AppenderBase<ILoggingEvent> {

  private final CounterMetric errorMetric;

  private final CounterMetric warnMetric;
  
  private final CounterMetric infoMetric;

  /**
   * Create with a given rateUnit.
   */
  public LogMetricAppender() {

    MetricName errorName = new MetricName("app", "log","error");
    errorMetric = MetricManager.getCounterMetric(errorName);

    MetricName warnName = new MetricName("app", "log", "warn");
    warnMetric = MetricManager.getCounterMetric(warnName);
    
    MetricName infoName = new MetricName("app", "log", "info");
    infoMetric = MetricManager.getCounterMetric(infoName);
  }

  /**
   * Increment the warning or error counters.
   */
  @Override
  protected void append(ILoggingEvent event) {

    switch (event.getLevel().toInt()) {
    case Level.WARN_INT:
      warnMetric.markEvent();
      break;
    case Level.ERROR_INT:
      errorMetric.markEvent();
      break;
    case Level.INFO_INT:
      infoMetric.markEvent();
      break;
    default:
      // not interested in any other logging events
      break;
    }
  }

}