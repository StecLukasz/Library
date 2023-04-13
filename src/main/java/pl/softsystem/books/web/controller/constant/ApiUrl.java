package pl.softsystem.books.web.controller.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiUrl {
    private final String BASE = "/api";
    public final String INFO = BASE + "/info";
    public final String USERS = BASE + "/users";

    @UtilityClass
    public class Book {
        public final String BASE = ApiUrl.BASE + "/books";
        public final String FOR_USER = "/books-for-user";
        public final String RESERVED_FOR_USER = "/reserved-books-for-user";
        public final String RESERVED_FOR_ADMIN = "/reserved-signatures-for-admin";
        public final String IS_BOOK_RESERVED_BY_USER = "/is-book-reserved-by-user";
        public final String RESERVE = "/reserve";
        public final String CANCEL_RESERVED = "/cancel-reserved";
        public final String CANCEL_SIGNATURE_RESERVATION = "/cancel-signature-reservation";
        public final String READY_SIGNATURE_RESERVATION = "/ready-signature-reservation";
        public final String BORROW_SIGNATURE = "/borrow-signature";
        public final String SEARCH = "/search";
        public final String SEARCH_WITH_GENRE_LIST = "/search-with-genre-list";
    }

    @UtilityClass
    public class Schedule{
        public final String RUN_SCHEDULER_AVAILABLE_AFTER_ONE_WEEK = "/scheduler";
    }

    @UtilityClass
    public class ManualTrigger {
        public final String BASE = ApiUrl.BASE + "/manual-trigger";
        public final String SEND_TEST_NOTIFICATION = "/send-test-notification";
        public final String SEND_REQUEST_DENIED_NOTIFICATION = "/send-request-denied-notification";
    }
}
