    package com.projeto_nuvem.projeto_nuvem.service;

    import java.util.List;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import com.projeto_nuvem.projeto_nuvem.model.FlashCard;
    import com.projeto_nuvem.projeto_nuvem.repository.FlashCardRepository;

    @Service
    public class FlashCardService {

        @Autowired
        private FlashCardRepository flashCardRepository;

        public List<FlashCard> getAllFlashCards() {
            return flashCardRepository.findAll();
        }

        public List<FlashCard> getFlashCardsBySubject(String subject) {
            return flashCardRepository.findBySubject(subject);
        }

        public List<FlashCard> getFlashCardsByCategory(String category) {
            return flashCardRepository.findByCategory(category);
        }

        public List<FlashCard> getFlashCardsByDifficulty(String difficulty) {
            return flashCardRepository.findByDifficulty(difficulty);
        }

        public FlashCard createFlashCard(FlashCard flashCard) {
            return flashCardRepository.save(flashCard);
        }

        public FlashCard getFlashCardById(Long id) {
            return flashCardRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("FlashCard not found with id " + id));
        }

        public FlashCard updateFlashCard(Long id, FlashCard flashCardDetails) {
            FlashCard flashCard = flashCardRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("FlashCard not found with id " + id));

            flashCard.setQuestion(flashCardDetails.getQuestion());
            flashCard.setAnswer(flashCardDetails.getAnswer());
            flashCard.setSubject(flashCardDetails.getSubject());
            flashCard.setDifficulty(flashCardDetails.getDifficulty());
            flashCard.setCategory(flashCardDetails.getCategory());

            return flashCardRepository.save(flashCard);
        }

        public void deleteFlashCard(Long id) {
            FlashCard flashCard = flashCardRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("FlashCard not found with id " + id));
            flashCardRepository.delete(flashCard);
        }


    }
