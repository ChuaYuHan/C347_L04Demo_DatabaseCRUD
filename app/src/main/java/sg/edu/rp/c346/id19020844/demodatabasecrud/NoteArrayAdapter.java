package sg.edu.rp.c346.id19020844.demodatabasecrud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NoteArrayAdapter extends ArrayAdapter<Note> {

    private ArrayList<Note> note;
    private Context context;
    private TextView tvID, tvContent;

    public NoteArrayAdapter(Context context, int resource, ArrayList<Note> objects) {
        super(context, resource, objects);
        this.context = context;
        note = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);
        tvID = (TextView) rowView.findViewById(R.id.tvID);
        tvContent = (TextView) rowView.findViewById(R.id.tvContent);

        Note currentNote = note.get(position);
        String id = String.valueOf(currentNote.getId());
        tvID.setText(id);

        String content = currentNote.getNoteContent();
        tvContent.setText(content);

        return rowView;
    }
}
