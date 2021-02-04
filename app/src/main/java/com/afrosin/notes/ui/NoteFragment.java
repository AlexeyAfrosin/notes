package com.afrosin.notes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrosin.notes.NoteDetailActivity;
import com.afrosin.notes.NoteDetailsFragment;
import com.afrosin.notes.R;
import com.afrosin.notes.data.Note;
import com.afrosin.notes.data.NoteCardsSource;
import com.afrosin.notes.data.NoteCardsSourceImp;

public class NoteFragment extends Fragment {

    private boolean isLandscape;
    private Note currentNote;
    NoteCardsSource noteCardsSource;
    NoteAdapter noteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        RecyclerView notesRecyclerView = view.findViewById(R.id.notesRecyclerView);

        Resources resources = getResources();
        Context context = getContext();

        noteCardsSource = new NoteCardsSourceImp(resources).init();
        initRecyclerView(notesRecyclerView, noteCardsSource);
        initItemDecoration(notesRecyclerView, resources, context);

        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initItemDecoration(RecyclerView notesRecyclerView, Resources resources, Context context) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.note_separator, null));
        notesRecyclerView.addItemDecoration(itemDecoration);
    }

    private void initRecyclerView(RecyclerView notesRecyclerView, NoteCardsSource noteCardsSource) {
        // Эта установка служит для повышения производительности системы
        notesRecyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notesRecyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        noteAdapter = new NoteAdapter(noteCardsSource);
        notesRecyclerView.setAdapter(noteAdapter);
        noteAdapter.setItemClickListener((view, note) -> showNoteDetails(note));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(NoteDetailsFragment.ARG_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }


    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {

            currentNote = savedInstanceState.getParcelable(NoteDetailsFragment.ARG_NOTE);
        } else {
            if (noteCardsSource != null && noteCardsSource.size() > 0) {
                currentNote = noteCardsSource.getCardData(0);
            }
        }

        if (isLandscape) {
            showNoteDetails(currentNote);
        }
    }

    private void showNoteDetails(Note currentNote) {
        this.currentNote = currentNote;
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

}