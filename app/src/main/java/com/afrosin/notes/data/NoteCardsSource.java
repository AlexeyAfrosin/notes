package com.afrosin.notes.data;

public interface NoteCardsSource {
    Note getCardData(int position);

    int size();

    void deleteCardData(int position);

    void updateCardData(int position, Note note);

    void addCardData(Note note);

    void clearCardData();
}
