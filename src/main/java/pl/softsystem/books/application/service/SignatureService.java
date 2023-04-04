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


    public List<ReservedSignaturesForAdminDTO> getReservedSignaturesForAdmin() {
        List<Signature> signatures = signatureRepository.findAll();
        List<ReservedSignaturesForAdminDTO> ReservedSignaturesForAdminDTOS = new ArrayList<>();
        List<Book> books = bookRepository.findAllByOrderByTitle();

        for (int i = 0; i < signatures.size(); i++) {
            if (getLatestStatusForSignature(signatures.get(i)).equals("reserved") ||
                    getLatestStatusForSignature(signatures.get(i)).equals("ready")) {
                ReservedSignaturesForAdminDTO reservedSignaturesForAdminDTO = new ReservedSignaturesForAdminDTO();
                String titleBySignatureIf = getTitleBySignatureId(books, (long) i + 1);
                reservedSignaturesForAdminDTO.setId(signatures.get(i).getId());
                reservedSignaturesForAdminDTO.setTitle(titleBySignatureIf);
                reservedSignaturesForAdminDTO.setBookSignature(signatures.get(i).getBookSignature());
                reservedSignaturesForAdminDTO.setUsername(getUsernameForLatestStatus(signatures.get(i)));
                reservedSignaturesForAdminDTO.setStatus(getLatestStatusForSignature(signatures.get(i)));
                ReservedSignaturesForAdminDTOS.add(reservedSignaturesForAdminDTO);
            }
        }
        return ReservedSignaturesForAdminDTOS;
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
