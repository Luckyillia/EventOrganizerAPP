package com.example.eventorganizerapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
    private TextView tvCounterEvents;
    private final String PREFS_NAME = "AppPrefs";
    private final String KEY_SAVED_TEXT = "DRAFT_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Inicjalizacja widoków formularza
        etEventName = findViewById(R.id.etEventName);
        etEventName.setText(prefs.getString(KEY_SAVED_TEXT, ""));
        // Inicjalizacja TextView
        tvCounterEvents = findViewById(R.id.tvCounterEvents);

        // Inicjalizacja RecyclerView
        rvEvents  = findViewById(R.id.rvEvents);
        EventList = new ArrayList<>();
        EventList.add(new Event(0,"Spotkanie organizacyjne"));
        EventList.add(new Event(1,"Warsztaty Android"));
        EventList.add(new Event(2,"Konferencja technologiczna"));
        adapter   = new EventAdapter(EventList,this);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvEvents.getContext(),
                ((LinearLayoutManager) rvEvents.getLayoutManager()).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        rvEvents.addItemDecoration(dividerItemDecoration);

        // Listener przycisku DODAJ
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> addEvent());
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> clearEvents());
        loadEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // prefs: obiekt reprezentujacy plik z zapisanymi preferencjami aplikacji
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // editor: "pisak" do modyfikowania danych w prefs
        SharedPreferences.Editor editor = prefs.edit();

        // Pobieramy aktualna zawartosc pola tekstowego
        String draftText = etEventName.getText().toString();
        // Wpisujemy wartosc do editora pod kluczem DRAFT_TEXT
        editor.putString(KEY_SAVED_TEXT, draftText);

        editor.apply();
    }

    /** Wczytuje wszystkie produkty z bazy i odświeża RecyclerView. */
    private void loadEvents() {
        adapter.notifyDataSetChanged();
        updateCounter();
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

    private void clearEvents(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Uwaga!");
        builder.setMessage("Czy napweno chcesz wyczyscic liste?");
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventList.clear();
                etEventName.setText("");
                loadEvents();
            }
        });
        builder.setNegativeButton("Nie", null);
        builder.show();
    }
    private void updateCounter(){
        if(EventList.isEmpty()){
            tvCounterEvents.setText("Liczba wydarzeń: 0");
            return;
        }
        tvCounterEvents.setText("Liczba wydarzeń: "+EventList.size());
    }
}
