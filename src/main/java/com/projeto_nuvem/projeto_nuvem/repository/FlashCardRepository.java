package com.projeto_nuvem.projeto_nuvem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto_nuvem.projeto_nuvem.model.FlashCard;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, Long> {

    Optional<FlashCard> findBySubject(String subject);

    Optional<FlashCard> findByCategory(String category);

    Optional<FlashCard> findByDifficulty(String difficulty);

}
