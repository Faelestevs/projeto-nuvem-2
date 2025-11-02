package com.projeto_nuvem.projeto_nuvem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto_nuvem.projeto_nuvem.model.FlashCard;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, Long> {

    List<FlashCard> findBySubject(String subject);

    List<FlashCard> findByCategory(String category);

    List<FlashCard> findByDifficulty(String difficulty);

}
