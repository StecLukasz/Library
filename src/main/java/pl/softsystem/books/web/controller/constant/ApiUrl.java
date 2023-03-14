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
    }


}
