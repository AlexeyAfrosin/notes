package com.afrosin.notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class NoteFragment extends Fragment {

    private boolean isLandscape;
    public static final String CURRENT_NOTE = "current_note_idx";
    private Note currentNote;
    private ArrayList<Note> notes;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNoteList(view);
    }

    private void initNoteList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] names = getResources().getStringArray(R.array.note_example_names);
        String[] texts = getResources().getStringArray(R.array.note_example_texts);
        ArrayList<Note> notes = new ArrayList<>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);

        for (int i = 0; i < names.length; i++) {
            Note tmpNote = new Note(names[i], texts[i], null);
            notes.add(tmpNote);
            TextView tv = new TextView(getContext());
            tv.setText(notes.get(i).getName());
            tv.setLayoutParams(params);
            layoutView.addView(tv);

            tv.setOnClickListener(v -> {
                currentNote = tmpNote;
                showNoteDetails(currentNote);

            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }


    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {

            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            if (notes != null && notes.size() > 0) {
                currentNote = notes.get(0);
            }
        }

        if (isLandscape) {
            showNoteDetails(currentNote);
        }
    }

    private void showNoteDetails(Note currentNote) {
        if (isLandscape) {
            showLandNoteDetails(currentNote);
        } else {
            showPortNoteDetails(currentNote);
        }

    }

    private void showLandNoteDetails(Note currentNote) {
        if (currentNote != null) {
            NoteDetailsFragment noteDetailsFragment = NoteDetailsFragment.newInstance(currentNote);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.note_details, noteDetailsFragment);  // замена фрагмента
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }


    private void showPortNoteDetails(Note currentNote) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NoteDetailActivity.class);
        intent.putExtra(NoteDetailsFragment.ARG_NOTE, currentNote);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }
}