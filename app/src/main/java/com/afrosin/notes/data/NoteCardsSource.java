package com.afrosin.notes.data;

public interface NoteCardsSource {
    Note getCardData(int position);

    int size();
}
