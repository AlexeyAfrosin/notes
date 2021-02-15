package com.afrosin.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogFragment extends DialogFragment {

    private static final String DATE_CREATED_TAG = "DATE_CREATED_TAG";

    private DatePicker dateCreatedDatePicker;
    private Navigation navigation;

    public DatePickerDialogFragment(Navigation navigation) {
        this.navigation = navigation;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_date_created_dialog, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        dateCreatedDatePicker = view.findViewById(R.id.dp_date_created);
        Button btCancel = view.findViewById(R.id.bt_cancel);
        Button btOk = view.findViewById(R.id.bt_ok);

        dateCreatedDatePicker = view.findViewById(R.id.dp_date_created);
        if (savedInstanceState != null) {
            Date dateCreated = new Date(savedInstanceState.getLong(DATE_CREATED_TAG));
            initDatePicker(dateCreated);
        }
        btCancel.setOnClickListener(v -> dismiss());
        btOk.setOnClickListener(v -> {
            dismiss();
            NoteDetailsFragment parentFragment = (NoteDetailsFragment) navigation.getFragmentByTag(NoteDetailsFragment.NOTE_DETAILS_FRAGMENT_TAG);
            parentFragment.onDatePickerDialogFragmentResult(getDateFromDatePicker());
        });

    }

    private void initDatePicker(Date dateCreated) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateCreated);
        dateCreatedDatePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }

    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, dateCreatedDatePicker.getYear());
        cal.set(Calendar.MONTH, dateCreatedDatePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, dateCreatedDatePicker.getDayOfMonth());
        return cal.getTime();
    }
}