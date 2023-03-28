package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.softsystem.books.domain.reservedSignaturesForAdminDTO;
import pl.softsystem.books.application.service.SignatureService;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class SignatureController {

    private final SignatureService signatureService;

    @GetMapping(ApiUrl.Book.RESERVED_FOR_ADMIN)
    public List<reservedSignaturesForAdminDTO> getReservedSignaturesForAdmin() {
        return signatureService.getReservedSignaturesForAdmin();
    }


}
