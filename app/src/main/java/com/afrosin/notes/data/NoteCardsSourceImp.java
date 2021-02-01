package com.afrosin.notes.data;

import android.content.res.Resources;

import com.afrosin.notes.R;

import java.util.ArrayList;

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
            notesDataSource.add(new Note(names[i], texts[i], null));
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
}
