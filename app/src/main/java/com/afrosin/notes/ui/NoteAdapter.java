package com.afrosin.notes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrosin.notes.R;
import com.afrosin.notes.data.Note;
import com.afrosin.notes.data.NoteCardsSource;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private NoteCardsSource dataSource;
    private OnItemClickListener itemClickListener;  // Слушатель будет устанавливаться извне


    public NoteAdapter(NoteCardsSource dataSource) {
        this.dataSource = dataSource;
    }

    // Этот класс хранит связь между данными и элементами View
    // Сложные данные могут потребовать несколько View на один пункт списка
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_note_item);
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, dataSource.getCardData(getAdapterPosition()));
                }

            });
        }


        public TextView getTextView() {
            return textView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Note note);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Создаём новый элемент пользовательского интерфейса
        // Через Inflater
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item, viewGroup, false);
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder viewHolder, int positionIdx) {
        viewHolder.getTextView().setText(dataSource.getCardData(positionIdx).getName());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }


}
