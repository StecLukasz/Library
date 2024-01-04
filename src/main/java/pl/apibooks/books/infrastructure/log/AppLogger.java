package pl.apibooks.books.infrastructure.log;

import lombok.experimental.UtilityClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class AppLogger {
    public final Logger ERROR = LoggerFactory.getLogger("ERROR");
    public final Logger MAIL = LoggerFactory.getLogger("MAIL");
    public final Logger SCHEDULE = LoggerFactory.getLogger("SCHEDULE");
    public final Logger INFO = LoggerFactory.getLogger("INFO");
}

