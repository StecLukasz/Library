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
        public final String FOR_USER = "/borrowed-books";
        public final String BORROWED_DATE = "/borrowed-date";
        public final String RESERVED_FOR_USER = "/reserved-books-for-user";
        public final String ADD = "/add";
        public final String FOR_ADMIN = "/admin-panel";
        public final String CANCEL_RESERVED = "/cancelReserved";
        public final String CANCEL_SIGNATURE_RESERVATION = "/cancelSignatureReservation";
        public final String READY_SIGNATURE_RESERVATION = "/readySignatureReservation";
        public final String BORROW_SIGNATURE_RESERVATION = "/borrowSignatureReservation";
        public final String SEARCH = "/search";

        public final String ADD_BOOK = "/add-book";
        public static final String EDIT_BOOK = "/edit-book/{bookId}";
        public final String GET_BOOK_BY_ID = "/{bookId}";
        public final String DELETE_BOOK = "/{bookId}";
    }
}
