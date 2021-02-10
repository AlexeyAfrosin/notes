package com.afrosin.notes.data;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class NoteCardSourceFirebaseImpl implements NoteCardsSource {

    private static final String NOTES_COLLECTION_NAME = "notes";
    private static final String TAG = "[NoteCardSrcFirebase]";

    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    private CollectionReference noteCollection = store.collection(NOTES_COLLECTION_NAME);

    private ArrayList<Note> notesDataSource = new ArrayList<>();

    @Override
    public NoteCardsSource init(final NoteCardsSourceResponse noteCardsSourceResponse) {
        noteCollection.orderBy(NoteCardDataMapping.Fields.DATE_CREATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> doc = document.getData();
                            String id = document.getId();
                            Note note = NoteCardDataMapping.toNoteCardData(id, doc);
                            notesDataSource.add(note);
                        }
                        Log.d(TAG, "success " + notesDataSource.size() + " count");
                        noteCardsSourceResponse.initialized(NoteCardSourceFirebaseImpl.this);
                    } else {
                        Log.d(TAG, "error: get failed with: ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "error: get failed with: ", e))
        ;
        return this;
    }

    @Override
    public Note getNoteCardData(int position) {
        return notesDataSource.get(position);
    }

    @Override
    public int size() {
        if (notesDataSource == null) {
            return 0;
        }
        return notesDataSource.size();
    }

    @Override
    public void deleteNoteCardData(int position) {
        noteCollection.document(notesDataSource.get(position).getId()).delete();
        notesDataSource.remove(position);
    }

    @Override
    public void updateNoteCardData(int position, Note note) {
        String id = note.getId();
        noteCollection.document(id).set(NoteCardDataMapping.toDocument(note));
    }

    @Override
    public void addNoteCardData(final Note note) {
        noteCollection.add(NoteCardDataMapping.toDocument(note))
                .addOnSuccessListener(documentReference -> note.setId(documentReference.getId()));
    }

    @Override
    public void clearNoteCardData() {
        for (Note note : notesDataSource) {
            noteCollection.document(note.getId()).delete();
        }
    }
}
