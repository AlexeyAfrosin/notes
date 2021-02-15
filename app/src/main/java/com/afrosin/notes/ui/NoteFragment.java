package com.afrosin.notes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrosin.notes.MainActivity;
import com.afrosin.notes.Navigation;
import com.afrosin.notes.NoteDetailsFragment;
import com.afrosin.notes.R;
import com.afrosin.notes.data.Note;
import com.afrosin.notes.data.NoteCardSourceFirebaseImpl;
import com.afrosin.notes.data.NoteCardsSource;
import com.afrosin.notes.observe.Observer;
import com.afrosin.notes.observe.Publisher;

public class NoteFragment extends Fragment {

    private static final int MY_DEFAULT_ANIMATION_DURATION = 1000;
    private static final String NOTE_DETAILS_FRAGMENT_TAG = "NOTE_DETAILS_FRAGMENT_TAG";

    private boolean isLandscape;
    private Note currentNote;
    private NoteCardsSource noteCardsSource;
    private NoteAdapter noteAdapter;
    private RecyclerView notesRecyclerView;
    private boolean moveToFirstPosition;
    private Navigation navigation;
    private Publisher publisher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        initView(view);
        setHasOptionsMenu(true);
        noteCardsSource = new NoteCardSourceFirebaseImpl().init(noteCardsSource -> noteAdapter.notifyDataSetChanged());
        noteAdapter.setDataSource(noteCardsSource);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.m_action_add:
                navigation.addFragment(NoteDetailsFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                                        @Override
                                        public void updateCardData(Note note) {
                                            noteCardsSource.addNoteCardData(note);
                                            noteAdapter.notifyItemInserted(noteCardsSource.size() - 1);
                                            moveToFirstPosition = true;
                                        }
                                    }
                );

                return true;
            case R.id.m_action_update:
                final int updatePosition = noteAdapter.getMenuPosition();

                navigation.addFragment(NoteDetailsFragment.newInstance(noteCardsSource.getNoteCardData(updatePosition)), true);
                publisher.subscribe(new Observer() {
                                        @Override
                                        public void updateCardData(Note note) {
                                            noteCardsSource.updateNoteCardData(updatePosition, note);
                                            noteAdapter.notifyItemInserted(noteCardsSource.size() - 1);
                                            noteAdapter.notifyItemChanged(updatePosition);
                                        }
                                    }
                );

                return true;
            case R.id.m_action_delete:
                final int deletePosition = noteAdapter.getMenuPosition();
                deleteNoteDetailFragment(noteCardsSource.getNoteCardData(deletePosition));
                noteCardsSource.deleteNoteCardData(deletePosition);
                noteAdapter.notifyItemRemoved(deletePosition);
                return true;
            case R.id.m_action_clear_all:
                noteCardsSource.clearNoteCardData();
                noteAdapter.notifyDataSetChanged();
                return true;
        }

        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    private void initView(View view) {
        Resources resources = getResources();
        Context context = getContext();
        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);
//        noteCardsSource = new NoteCardsSourceImp(resources).init();
        initRecyclerView();
        initItemDecoration(notesRecyclerView, resources, context);
        initItemAnimation(notesRecyclerView);

        if (moveToFirstPosition && noteCardsSource.size() > 0) {
            notesRecyclerView.smoothScrollToPosition(0);
            moveToFirstPosition = false;
        }
    }

    private void initItemAnimation(RecyclerView notesRecyclerView) {
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(MY_DEFAULT_ANIMATION_DURATION);
        itemAnimator.setRemoveDuration(MY_DEFAULT_ANIMATION_DURATION);
        notesRecyclerView.setItemAnimator(itemAnimator);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initItemDecoration(RecyclerView notesRecyclerView, Resources resources, Context context) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.note_separator, null));
        notesRecyclerView.addItemDecoration(itemDecoration);
    }

    private void initRecyclerView() {
        // Эта установка служит для повышения производительности системы
        notesRecyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notesRecyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        noteAdapter = new NoteAdapter(this);
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
                currentNote = noteCardsSource.getNoteCardData(0);
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
            addNoteDetailsFragment(R.id.note_details, currentNote, false);
        }
    }

    private void showPortNoteDetails(Note currentNote) {
        addNoteDetailsFragment(R.id.main_fragment_container, currentNote, true);
    }

    private void addNoteDetailsFragment(int containerId, Note currentNote, boolean useBackStack) {
        NoteDetailsFragment noteDetailsFragment = NoteDetailsFragment.newInstance(currentNote);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, noteDetailsFragment, NOTE_DETAILS_FRAGMENT_TAG);  // замена фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View view, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.card_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    private void deleteNoteDetailFragment(Note note) {
        if (isLandscape && note == currentNote) {
            navigation.deleteFragmentByTag(NOTE_DETAILS_FRAGMENT_TAG);
        }
    }

    private FragmentManager getSupportFragmentManager() {
        return requireActivity().getSupportFragmentManager();
    }

}