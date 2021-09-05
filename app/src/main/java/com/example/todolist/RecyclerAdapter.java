package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.database.DataConverter;
import com.example.todolist.database.TodoTaskEntity;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private List<TodoTaskEntity> todoTaskEntities;
    private Context context;
    public static final String ROW_ID = "row_primary_id";
    Boolean showMenu = false;
    public static final String MENU_KEY = "show_menu_key";


    public RecyclerAdapter(Context context, List<TodoTaskEntity> todoTaskEntities) {
        this.context = context;
        this.todoTaskEntities = todoTaskEntities;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.viewholder_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerAdapter.MyViewHolder holder, int position) {
        TodoTaskEntity task = todoTaskEntities.get(position);
        Bitmap bitmap = DataConverter.convertToBitmap(task.getImgInBytes());
        holder.toDoTxtView.setText(task.getToDoTask());
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return todoTaskEntities.size();
    }

    // View holder class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView toDoTxtView;
        private ImageView imageView;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            
            toDoTxtView = itemView.findViewById(R.id.textViewTask);
            imageView = itemView.findViewById(R.id.imageView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int taskId = todoTaskEntities.get(getAdapterPosition()).getTaskId();
                    Log.d("test","rowid: "+String.valueOf(taskId));
                    Intent intent = new Intent(context,AddTodo.class);
                    intent.putExtra(ROW_ID,taskId);
                    intent.putExtra(MENU_KEY,showMenu);
                    context.startActivity(intent);
                }
            });
        }

    }

    // method that sets the list to be display in recycler view
    public void setList(List<TodoTaskEntity> todoTaskEntities) {
        this.todoTaskEntities = todoTaskEntities;
        notifyDataSetChanged();
    }
}
