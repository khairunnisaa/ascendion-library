package com.ascendion.library.controller;

import com.ascendion.library.entity.Borrower;
import com.ascendion.library.service.BorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Borrower registerBorrower(@Valid @RequestBody Borrower borrower) {
        return borrowerService.registerBorrower(borrower);
    }

    @GetMapping("/{borrowerId}")
    public Borrower getBorrowerById(@PathVariable Long borrowerId) {
        return borrowerService.getById(borrowerId);
    }

    @GetMapping
    public List<Borrower> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }
}
