package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import us.zoom.sdksample.R;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    //init arraylist
    ArrayList<String> arrayListMember;
    //construct

    public MemberAdapter(ArrayList<String> arrayListMember){
        this.arrayListMember = arrayListMember;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_member,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //set member name
        holder.tvName.setText(arrayListMember.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayListMember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //init variable
        TextView tvName;
        Context context;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,""+arrayListMember.get(getAdapterPosition()),Toast.LENGTH_SHORT);
                    System.out.println(arrayListMember.get(getPosition()));
                }
            });

        }
    }
}
