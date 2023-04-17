package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignatureService {

    private final SignatureRepository signatureRepository;
    private final BookRepository bookRepository;


    public List<SignatureDTO> getSignaturesForAdminList() {
        List<Signature> signatures = signatureRepository.findAll();
        List<SignatureDTO> adminSignatureDTOS = new ArrayList<>();
        List<Book> books = bookRepository.findAllByOrderByTitle();

        for (int i = 0; i < signatures.size(); i++) {
            SignatureDTO signatureDTO = new SignatureDTO();
                String titleBySignatureIf = getTitleBySignatureId(books, signatures.get(i).getId());
                signatureDTO.setId(signatures.get(i).getId());
                signatureDTO.setTitle(titleBySignatureIf);
                signatureDTO.setBookId(signatures.get(i).getBookId());
                signatureDTO.setBookSignature(signatures.get(i).getBookSignature());
                signatureDTO.setUsername(getUsernameForLatestStatus(signatures.get(i)));
                signatureDTO.setStatus(getLatestStatusForSignature(signatures.get(i)));
                adminSignatureDTOS.add(signatureDTO);
            }
        return adminSignatureDTOS;
    }

    public String getLatestStatusForSignature(Signature signature) {
        List<Borrowed> borrowedList = signature.getBorrowedBookList();
        if (borrowedList == null || borrowedList.isEmpty()) return null;
        Borrowed latestBorrowed = borrowedList.get(0);
        for (Borrowed borrowed : borrowedList) {
            if (borrowed.getStatusDate().compareTo(latestBorrowed.getStatusDate()) > 0) {
                latestBorrowed = borrowed;
            }
        }
        return latestBorrowed.getStatus();
    }

    public String getUsernameForLatestStatus(Signature signature) {
        List<Borrowed> borrowedList = signature.getBorrowedBookList();
        if (borrowedList == null || borrowedList.isEmpty()) return null;
        Borrowed latestBorrowed = borrowedList.get(0);
        for (Borrowed borrowed : borrowedList) {
            if (borrowed.getStatusDate().compareTo(latestBorrowed.getStatusDate()) > 0) {
                latestBorrowed = borrowed;
            }
        }
        return latestBorrowed.getLogin();
    }

    public String getTitleBySignatureId(List<Book> books, Long signatureId) {
        for (Book book : books) {
            for (Signature signature : book.getSignatures()) {
                if (signature.getId().equals(signatureId)) {
                    return book.getTitle();
                }
            }
        }
        return null;
    }
}
