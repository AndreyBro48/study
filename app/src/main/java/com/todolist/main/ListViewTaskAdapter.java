package com.todolist.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.todolist.models.Task;

import java.util.List;

public class ListViewTaskAdapter extends ArrayAdapter<Task> {

    public ListViewTaskAdapter(@NonNull Context context, @NonNull List<Task> tasks) {
        super(context, R.layout.row_list_view, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_list_view, parent, false);
        TextView title = row.findViewById(R.id.textView);
        title.setText(getItem(position).getDefinition());
        return row;
    }
}
