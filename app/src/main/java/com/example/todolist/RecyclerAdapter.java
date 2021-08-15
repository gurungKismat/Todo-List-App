package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.database.TodoTaskEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private List<TodoTaskEntity> todoTaskEntities;
    public static List<TodoTaskEntity> checkedList = new ArrayList<>();
    private Context context;
    private DeleteTask mDeleteTask;
    public static final String ROW_ID = "row_primary_id";
    Boolean showMenu = false;
    public static final String MENU_KEY = "show_menu_key";


    public RecyclerAdapter(Context context, List<TodoTaskEntity> todoTaskEntities,DeleteTask mDeleteTask) {
        this.context = context;
        this.todoTaskEntities = todoTaskEntities;
        this.mDeleteTask = mDeleteTask;
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
        holder.toDoTxtView.setText(task.getToDoTask());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        holder.toDoTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taskId = todoTaskEntities.get(position).getTaskId();
                Log.d("test","rowid: "+String.valueOf(taskId));
                Intent intent = new Intent(context,AddTodo.class);
                intent.putExtra(ROW_ID,taskId);
                intent.putExtra(MENU_KEY,showMenu);
                context.startActivity(intent);
            }
        });
    }


    public interface DeleteTask {
        void deleteTask(List<TodoTaskEntity> checkedTaskList);
    }

    @Override
    public int getItemCount() {
        return todoTaskEntities.size();
    }

    public void setList(List<TodoTaskEntity> todoTaskEntities) {
       this.todoTaskEntities = todoTaskEntities;
       notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView toDoTxtView;
        CheckBox checkBox;
        
        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            
            toDoTxtView = itemView.findViewById(R.id.textViewTask);
            checkBox = itemView.findViewById(R.id.checkBoxTask);

        }
    }
}
