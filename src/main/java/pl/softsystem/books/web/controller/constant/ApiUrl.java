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
        public final String RESERVE = "/reserve";
        public final String CANCEL_RESERVED = "/cancelReserved";
        public final String CANCEL_SIGNATURE_RESERVATION = "/cancelSignatureReservation";
        public final String READY_SIGNATURE_RESERVATION = "/readySignatureReservation";
        public final String SEARCH = "/search";
    }
}
