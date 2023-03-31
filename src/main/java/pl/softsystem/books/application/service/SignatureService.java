//package pl.softsystem.books.application.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import pl.softsystem.books.domain.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class SignatureService {
//
//    @Autowired
//    private SignatureRepository signatureRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    public Signature addSignature(SignatureDTO signatureDTO) {
//        Signature signature = new Signature();
//        signature.setBookSignature(signatureDTO.getBookSignature());
//        signature.setBookId(signatureDTO.getBookId());
//        signature = signatureRepository.save(signature);
//
//        Optional<Book> bookOptional = bookRepository.findById(signatureDTO.getBookId());
//        bookOptional.ifPresent(book -> {
//            List<Signature> signatures = book.getSignatures();
////            signatures.add(signature);
//            book.setSignatures(signatures);
//            bookRepository.save(book);
//        });
//
//        return signature;
//    }
//
//    public List<Signature> getSignaturesByBookId(Long id) {
//        return null;
//    }
//}
