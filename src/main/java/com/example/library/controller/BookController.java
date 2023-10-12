package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public Book show(@PathVariable Long id) {
        return repository.findById(id).get();
    }

    @PostMapping("/books")
        public Book create(@RequestBody Book book) {
        return repository.save(book);
    }

    @PostMapping("/books/search")
    public List<Book> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return repository.findByTitleContainingOrDescriptionContaining(searchTerm, searchTerm);
    }

    @PutMapping("/books/{id}")
    public Book update(@RequestBody Book book, @PathVariable Long id) {
        Book updatedBook = repository.findById(id).get();
        updatedBook.setTitle(book.getTitle());
        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setDescription(book.getDescription());
        return repository.save(updatedBook);
    }

    @DeleteMapping("books/{id}")
    public boolean delete(@PathVariable Long id){
        repository.deleteById(id);
        return true;
    }


}
