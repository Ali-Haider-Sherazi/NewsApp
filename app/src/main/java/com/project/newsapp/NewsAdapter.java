package com.project.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class NewsAdatpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final RecylerViewInterface recylerViewInterface;

    private ArrayList<NewsInfo> books = new ArrayList<>();
    private Context mContext;
    public int  size;

    public NewsAdatpter(Context context, ArrayList<NewsInfo> books, RecylerViewInterface recylerViewInterface)
    {
       /* int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, books.get(0).getBookName()+" in adapter ", duration);
        toast.show();*/
        mContext = context;
        this.books = books;
        this.recylerViewInterface = recylerViewInterface;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new BookViewHolder(view, recylerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e("this is working ", " Wrong");
        onBindViewHolders((BookViewHolder) holder, position);
    }

    public void onBindViewHolders(@NonNull BookViewHolder holder, int position) {
        holder.bindBook(books.get(position));
        Log.e("this is working ", " correct");
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public NewsInfo getItem(int position) {
        Log.e("position is ", " "+ position+" and value is "+books.get(position));
        return books.get(position);
    }

    public ArrayList<NewsInfo> getBook()
    {
        return books;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView Name,type,date;
        public BookViewHolder(View itemView, RecylerViewInterface recylerViewInterface) {

            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            type = (TextView) itemView.findViewById(R.id.type);
            
            itemView.setOnClickListener(v-> {
                Log.e(" Clicked pos is  "," ");
            });

            //Fetching the Position of item selected in recycler view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recylerViewInterface != null)
                    {
                        int pos = getAdapterPosition();
                        Log.e(" Clicked pos is  "," "+ pos +" bOkk Size "+ books.size());
                        if(pos!= RecyclerView.NO_POSITION)
                        {
                            recylerViewInterface.onItemClick(pos);
                            size= books.size();
                        }
                    }
                    else
                    {
                        Log.e(" Clicked pos is  "," wrong");
                    }

                }

            });



        }
        @SuppressLint("LongLogTag")
        public void bindBook(NewsInfo newsInfo)
        {
            date.setText("Date: "+newsInfo.getDate());
            Name.setText("Title: "+newsInfo.getName());
            type.setText("Type: "+newsInfo.getType());



        }


    }
}
