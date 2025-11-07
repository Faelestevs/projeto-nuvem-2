const API_URL = "http://35.170.95.1:8080/api/flashcards";

export async function handler() {

  try {
    const response = await fetch(API_URL);

    if (!response.ok) {
      throw new Error(`Erro na requisição: ${response.status}`);
    }

    const flashcards = await response.json();

    const numFlashcards = flashcards.length;

    const numPerCategory = getQuantity(flashcards, "category");

    const numPerDifficulty = getQuantity(flashcards, "difficulty");

    const numPerSubject = getQuantity(flashcards, "subject");

    const summary = {
      totalFlashcards: numFlashcards,
      totalPorCategoria: numPerCategory,
      totalPorMatéria: numPerSubject,
      totalPorDificuldade: numPerDifficulty,
      dataRelatorio: new Date().toISOString()
    };

    return summary;

  } catch (error) {
    console.error("Erro ao obter flashcards:", error);
    return {
      statusCode: 500,
      body: JSON.stringify({
        error: "Erro ao gerar relatório",
        message: error.message
      })
    };
  };
};

function getQuantity(flashcards, data) {
  return flashcards.reduce((acc, card) => {
    const key = card[data];
    acc[key] = (acc[key] || 0) + 1;
    return acc;
  }, {});
}