package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.*;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
public class BookAuthorService {

    private final BookAuthorRepository bookAuthorRepository;

    @Transactional
    public void  removeBookAuthor(Long bookId){
                bookAuthorRepository.deleteByBookId(bookId);
    }


}
