package com.afrosin.notes.data;

public interface NoteCardsSource {

    NoteCardsSource init(NoteCardsSourceResponse noteCardsSourceResponse);

    Note getNoteCardData(int position);

    int size();

    void deleteNoteCardData(int position);

    void updateNoteCardData(int position, Note note);

    void addNoteCardData(Note note);

    void clearNoteCardData();
}
