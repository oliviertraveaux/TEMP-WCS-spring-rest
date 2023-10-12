package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class BookController {

    private BookRepository repository;

    public BookController(BookRepository repositoryInjected) {
        this.repository = repositoryInjected;
    }

    @GetMapping("/books")
    public List<Book> index() {
        return repository.findAll();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> show(@PathVariable Long id) {
        Optional<Book> bookOptional = repository.findById(id);
        return bookOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
        // equivalent à
        //        if (bookOptional.isPresent()) {
        //            return ResponseEntity.ok(bookOptional.get());
        //        } else {
        //            return ResponseEntity.status(404).body(null);
        //        }
    }

    @PostMapping("/books")
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book createdBook = repository.save(book);
        return  ResponseEntity.ok().body(createdBook);
    }

    @PostMapping("/books/search")
    public ResponseEntity<?> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        List<Book> result = repository.findByTitleContainingOrDescriptionContaining(searchTerm, searchTerm);
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("Aucun livre trouvé");
        } else {
            return ResponseEntity.ok().body(result);
        }
    }

    @PutMapping("/books/{id}")
    public Book update(@RequestBody Book book, @PathVariable Long id) {
        Book bookToUpdate = repository.findById(id).get();
        if (book.getTitle() != null) {
            bookToUpdate.setTitle(book.getTitle());
        }
        if (book.getAuthor() != null) {
            bookToUpdate.setAuthor(book.getAuthor());
        }
        if (book.getDescription() != null) {
            bookToUpdate.setDescription(book.getDescription());
        }
        return repository.save(bookToUpdate);
    }

    @DeleteMapping("books/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Optional<Book> bookOptional = repository.findById(id);
                if (bookOptional.isPresent()) {
                    repository.deleteById(id);
                    return ResponseEntity.ok().body("Le livre a été supprimé avec succès.");
                } else {
                    return ResponseEntity.status(404).body("Aucun livre trouvé");
                }
    }


}
