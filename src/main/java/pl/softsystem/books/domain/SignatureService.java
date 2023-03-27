package pl.softsystem.books.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignatureService {

    private final SignatureRepository signatureRepository;
    private final BookRepository bookRepository;

    public List<AdminSignatureReservedDTO> getReservedSignaturesForAdmin() {
        List<Signature> signatures = signatureRepository.findAll();
        List<AdminSignatureReservedDTO> adminSignatureReservedDTOS = new ArrayList<>();
        List<Book> books = bookRepository.findAllByOrderByTitle();

        for (int i = 0; i < signatures.size(); i++) {
            if (getLatestStatusForSignature(signatures.get(i)).equals("reserved") ||
                    getLatestStatusForSignature(signatures.get(i)).equals("ready")) {
                AdminSignatureReservedDTO adminSignatureReservedDTO = new AdminSignatureReservedDTO();
                String titleBySignatureIf = getTitleBySignatureId(books, (long) i + 1);
                adminSignatureReservedDTO.setId(signatures.get(i).getId());
                adminSignatureReservedDTO.setTitle(titleBySignatureIf);
                adminSignatureReservedDTO.setBookSignature(signatures.get(i).getBookSignature());
                adminSignatureReservedDTO.setUsername(getUsernameForLatestStatus(signatures.get(i)));
                adminSignatureReservedDTO.setStatus(getLatestStatusForSignature(signatures.get(i)));
                adminSignatureReservedDTOS.add(adminSignatureReservedDTO);
            }
        }
        return adminSignatureReservedDTOS;
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
