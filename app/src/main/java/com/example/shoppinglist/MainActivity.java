package com.example.shoppinglist;



import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> shoppingList;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shoppingList = new ArrayList<>();
        adapter = new TaskAdapter();

        ListView listView = findViewById(R.id.shoppingListView);
        listView.setAdapter(adapter);

        Button addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(v -> showAddItemDialog());

        listView.setOnItemClickListener((parent, view, position, id) -> removeItemWithConfirmation(position));
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить элемент");

        final android.widget.EditText input = new android.widget.EditText(this);
        builder.setView(input);

        builder.setPositiveButton("добавить", (dialog, which) -> {
            String item = input.getText().toString().trim();
            if (!item.isEmpty()) {
                shoppingList.add(item);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Элемент не может быть пустым", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void removeItemWithConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление элемента")
                .setMessage("Вы уверены, что хотите удалить этот элемент?")
                .setPositiveButton("Да", (dialog, which) -> {
                    shoppingList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Элемент удалён", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Нет", (dialog, which) -> dialog.cancel())
                .show();
    }

    private class TaskAdapter extends ArrayAdapter<String> {
        public TaskAdapter() {
            super(MainActivity.this, R.layout.list_item, shoppingList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            CheckBox checkBox = convertView.findViewById(R.id.taskCheckBox);
            TextView textView = convertView.findViewById(R.id.taskTextView);

            String task = getItem(position);
            textView.setText(task);

            checkBox.setOnCheckedChangeListener(null);
            boolean isCompleted = (textView.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) != 0;
            checkBox.setChecked(isCompleted);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            });

            return convertView;
        }
    }
}
