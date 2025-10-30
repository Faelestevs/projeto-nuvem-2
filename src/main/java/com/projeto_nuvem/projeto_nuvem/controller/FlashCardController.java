package com.projeto_nuvem.projeto_nuvem.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto_nuvem.projeto_nuvem.model.FlashCard;
import com.projeto_nuvem.projeto_nuvem.service.FlashCardService;

@RestController
@RequestMapping("/api/flashcards")
public class FlashCardController {

    @Autowired
    private FlashCardService flashCardService;

    @GetMapping
    public ResponseEntity<List<FlashCard>> getFlashCards(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty) {

        if (subject != null) {
            return ResponseEntity.ok(flashCardService.getFlashCardsBySubject(subject));
        } else if (category != null) {
            return ResponseEntity.ok(flashCardService.getFlashCardsByCategory(category));
        } else if (difficulty != null) {
            return ResponseEntity.ok(flashCardService.getFlashCardsByDifficulty(difficulty));
        } else {
            return ResponseEntity.ok(flashCardService.getAllFlashCards());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashCard> getFlashCardById(@PathVariable Long id) {
        FlashCard flashCard = flashCardService.getFlashCardById(id);
        if (flashCard != null) {
            return new ResponseEntity<>(flashCard, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<FlashCard> createFlashCard(@RequestBody FlashCard flashCard) {
        FlashCard createdFlashCard = flashCardService.createFlashCard(flashCard);
        return new ResponseEntity<>(createdFlashCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashCard> updateFlashCard(@PathVariable Long id, @RequestBody FlashCard flashCardDetails) {
        FlashCard updatedFlashCard = flashCardService.updateFlashCard(id, flashCardDetails);
        return new ResponseEntity<>(updatedFlashCard, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashCard(@PathVariable Long id) {
        flashCardService.deleteFlashCard(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
