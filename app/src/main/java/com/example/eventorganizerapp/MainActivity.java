package com.example.eventorganizerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView   rvEvents;
    private EventAdapter adapter;
    private List<Event>  EventList;

    private EditText etEventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja widoków formularza
        etEventName     = findViewById(R.id.etEventName);

        // Inicjalizacja RecyclerView
        rvEvents  = findViewById(R.id.rvEvents);
        EventList = new ArrayList<>();
        EventList.add(new Event(0,"Spotkanie organizacyjne"));
        EventList.add(new Event(1,"Warsztaty Android"));
        EventList.add(new Event(2,"Konferencja technologiczna"));
        adapter   = new EventAdapter(EventList);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvEvents.getContext(),
                ((LinearLayoutManager) rvEvents.getLayoutManager()).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        rvEvents.addItemDecoration(dividerItemDecoration);

        // Listener przycisku DODAJ
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> addEvent());
        loadEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    /** Wczytuje wszystkie produkty z bazy i odświeża RecyclerView. */
    private void loadEvents() {
        adapter.notifyDataSetChanged();
    }

    /** Waliduje formularz, dodaje produkt do bazy i odświeża listę. */
    private void addEvent() {
        String name = etEventName.getText().toString().trim();

        // Walidacja: nazwa nie może być pusta
        if (name.isEmpty()) {
            Toast.makeText(this, "Nazwa wydarzenia nie może być pusta", Toast.LENGTH_SHORT).show();
            return;
        }
        EventList.add(0,new Event(EventList.size(), name));

        etEventName.setText("");

        loadEvents();
    }
}
