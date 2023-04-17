package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.softsystem.books.domain.BookRepository;
import pl.softsystem.books.domain.SignatureDTO;
import pl.softsystem.books.application.service.SignatureService;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class SignatureController {

    private final BookRepository bookRepository;
    private final SignatureService signatureService;

    @GetMapping(ApiUrl.Book.FOR_ADMIN)
    public List<SignatureDTO> getSignaturesForAdminPanel() {
        return signatureService.getSignaturesForAdminList();
    }
}
