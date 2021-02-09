package com.afrosin.notes.data;

import android.content.res.Resources;

import com.afrosin.notes.R;

import java.util.ArrayList;
import java.util.Calendar;

public class NoteCardsSourceImp implements NoteCardsSource {
    private ArrayList<Note> notesDataSource;
    private Resources resources;

    public NoteCardsSourceImp(Resources resources) {
        this.notesDataSource = new ArrayList<>();
        this.resources = resources;
    }

    public NoteCardsSourceImp init() {

        String[] names = resources.getStringArray(R.array.note_example_names);
        String[] texts = resources.getStringArray(R.array.note_example_texts);

        for (int i = 0; i < names.length; i++) {
            notesDataSource.add(new Note(names[i], texts[i], Calendar.getInstance().getTime()));
        }
        return this;
    }

    @Override
    public Note getCardData(int position) {
        return notesDataSource.get(position);
    }

    @Override
    public int size() {
        return notesDataSource.size();
    }

    @Override
    public void deleteCardData(int position) {
        notesDataSource.remove(position);
    }

    @Override
    public void updateCardData(int position, Note note) {
        notesDataSource.set(position, note);
    }

    @Override
    public void addCardData(Note note) {
        notesDataSource.add(note);
    }

    @Override
    public void clearCardData() {
        notesDataSource.clear();
    }
}
