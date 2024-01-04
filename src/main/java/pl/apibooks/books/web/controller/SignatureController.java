package pl.apibooks.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.apibooks.books.domain.ReservedSignaturesForAdminDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.apibooks.books.domain.BookRepository;
import pl.apibooks.books.domain.SignatureDTO;
import pl.apibooks.books.application.service.SignatureService;
import pl.apibooks.books.web.controller.constant.ApiUrl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class SignatureController {

    private final SignatureService signatureService;

    @GetMapping(ApiUrl.Book.RESERVED_FOR_ADMIN)
    public List<ReservedSignaturesForAdminDTO> getReservedSignaturesForAdmin() {
        return signatureService.getReservedSignaturesForAdmin();
    }

    @GetMapping(ApiUrl.Book.FOR_ADMIN)
    public List<SignatureDTO> getSignaturesForAdminPanel() {
        return signatureService.getSignaturesForAdminList();
    }

    @GetMapping(ApiUrl.Book.BORROWED_BOOKS_FOR_ADMIN)
    public List<SignatureDTO> getSignaturesBorrowedForAdmin() {
        return signatureService.getSignaturesBorrowedForAdmin();
    }

}
