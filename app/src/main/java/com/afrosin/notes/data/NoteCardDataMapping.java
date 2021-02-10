package com.afrosin.notes.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteCardDataMapping {


    public static class Fields {
        public final static String NAME = "name";
        public final static String TEXT = "text";
        public final static String DATE_CREATED = "dateCreated";

    }

    public static Note toNoteCardData(String id, Map<String, Object> doc) {
        String name = (String) doc.get(Fields.NAME);
        String text = (String) doc.get(Fields.TEXT);
        Timestamp dateCreated = (Timestamp) doc.get(Fields.DATE_CREATED);
        Note note;
        if (dateCreated != null) {
            note = new Note(name, text, dateCreated.toDate());
        } else {
            note = new Note(name, text, null);
        }

        note.setId(id);

        return note;

    }

    public static Map<String, Object> toDocument(Note note) {
        Map<String, Object> document = new HashMap<>();

        document.put(Fields.NAME, note.getName());
        document.put(Fields.TEXT, note.getText());
        document.put(Fields.DATE_CREATED, note.getDateCreated());

        return document;
    }

}
