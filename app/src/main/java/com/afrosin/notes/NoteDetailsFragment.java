package com.afrosin.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.afrosin.notes.data.Note;
import com.afrosin.notes.observe.Publisher;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailsFragment extends Fragment {

    public static final String ARG_NOTE = "agr_note";
    public static final String DATE_PICKER_DIALOG_FRAGMENT_TAG = "DATE_PICKER_DIALOG_FRAGMENT_TAG";
    public static final String NOTE_DETAILS_FRAGMENT_TAG = "NOTE_DETAILS_FRAGMENT_TAG";
    private Note note;
    private Publisher publisher;
    EditText noteNameEditText;
    EditText noteTextEditText;
    TextView noteDateCreatedEditText;
    Date dateCreated;
    MainActivity activity;


    public static NoteDetailsFragment newInstance(Note note) {
        NoteDetailsFragment fragment = new NoteDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public static NoteDetailsFragment newInstance() {
        return new NoteDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }

    @Override
    public void onStop() {
        super.onStop();
        note = collectNote();
    }

    private Note collectNote() {
        String name = noteNameEditText.getText().toString();
        String text = noteTextEditText.getText().toString();

        Note tmpNote = new Note(name, text, dateCreated);
        if (note != null) {
            tmpNote.setId(note.getId());
            return tmpNote;
        }

        return tmpNote;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_details, container, false);

        initView(view);

        if (note != null) {
            fillView();
        }

        return view;
    }

    private void fillView() {
        noteNameEditText.setText(note.getName());
        noteTextEditText.setText(note.getText());
        noteDateCreatedEditText.setText(String.format(getResources().getString(R.string.tv_date_created), note.getDateCreatedStr()));
        dateCreated = note.getDateCreated();
//        initDatePicker(note.getDateCreated());
    }


    private void initView(View view) {
        noteNameEditText = view.findViewById(R.id.et_name);
        noteTextEditText = view.findViewById(R.id.et_text);
        noteDateCreatedEditText = view.findViewById(R.id.tv_note_date_created);
        noteDateCreatedEditText.setOnClickListener(v -> {
            DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(activity.getNavigation());
            datePickerDialogFragment.show(requireActivity().getSupportFragmentManager(), DATE_PICKER_DIALOG_FRAGMENT_TAG);
        });

    }

    public void onDatePickerDialogFragmentResult(Date dateCreated) {
        if (note != null) {
            note.setDateCreated(dateCreated);
            fillView();
        } else {
            this.dateCreated = dateCreated;
            noteDateCreatedEditText.setText(String.format(getResources().getString(R.string.tv_date_created), new SimpleDateFormat("dd-MM-yyyy").format(dateCreated)));
        }
    }
}