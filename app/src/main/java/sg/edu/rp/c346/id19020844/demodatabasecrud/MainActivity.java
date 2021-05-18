package sg.edu.rp.c346.id19020844.demodatabasecrud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etContent;
    Button btnInsert, btnEdit, btnRetrieve;
    TextView tvDBContent;
    ArrayList<Note> al;
    ListView lv;
    ArrayAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the variables with UI here
        al = new ArrayList<Note>();

        etContent = findViewById(R.id.etContent);
        btnInsert = findViewById(R.id.buttonInsert);
        btnEdit = findViewById(R.id.buttonEdit);
        btnRetrieve = findViewById(R.id.buttonRetrieve);
        tvDBContent = findViewById(R.id.tvDBContent);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = etContent.getText().toString();
                DBHelper db = new DBHelper(MainActivity.this);
                long inserted_id = db.insertNote(data);
                db.close();

                if (inserted_id != -1) {
                    Toast.makeText(MainActivity.this,
                            "Insert successful",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper db = new DBHelper(MainActivity.this);
                al.clear();
                al.addAll(db.getAllNotes());
                db.close();

                String text = "";
                for (int i = 0; i < al.size(); i++) {
                    Note tmp = al.get(i);
                    text = "ID:" + tmp.getId() + ", " + tmp.getNoteContent() + "\n";
                }
                tvDBContent.setText(text);
                aa.notifyDataSetChanged();
            }
        });

        lv = findViewById(R.id.lv);
        aa = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long identity) {
                Note target = al.get(position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("data", target);
                startActivityForResult(i, 9);
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note target = al.get(0);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("data", target);
                //startActivity(i);
                startActivityForResult(i, 9);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 9) {
            btnRetrieve.performClick();
        }
    }
}