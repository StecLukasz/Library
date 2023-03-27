package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.softsystem.books.domain.AdminSignatureReservedDTO;
import pl.softsystem.books.domain.SignatureService;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class SignatureController {

    private final SignatureService signatureService;

    @GetMapping(ApiUrl.Book.RESERVED_FOR_ADMIN)
    public List<AdminSignatureReservedDTO> getReservedSignaturesForAdmin() {
        return signatureService.getReservedSignaturesForAdmin();
    }
}
