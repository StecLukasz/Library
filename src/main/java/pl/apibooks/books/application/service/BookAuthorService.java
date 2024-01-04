package pl.apibooks.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.apibooks.books.domain.*;

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
