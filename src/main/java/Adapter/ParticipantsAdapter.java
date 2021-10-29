package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.R;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder>{
    List<User> users;
    Context context;
    boolean isSelectMode = false;
    private int checkedPosition = 0 ;
    ArrayList<User> selectItem = new ArrayList<>();

    public ParticipantsAdapter(Context context,List<User> users){
        this.context = context;
        this.users = users;
    }



    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new ParticipantsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.nameText.setText(users.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public User getSelected(){
        if(checkedPosition!=-1){
            return users.get(checkedPosition);
        }
        return null;
    }

    public ArrayList<User> getSelectItem(){
        if (selectItem !=null){
            return selectItem;
        }
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameText;
        ImageView userImageView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.user_name);
            userImageView =itemView.findViewById(R.id.checkImageView);

//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                   if (selectItem.contains(users.get(getAdapterPosition()))){
//                       userImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
//                       selectItem.remove(users.get(getAdapterPosition()));
//                   }else {
//                       userImageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
//                       selectItem.add(users.get(getAdapterPosition()));
//                   }
//                   if (selectItem.size() == 0 ){
//                       isSelectMode = false;
//                   }
//                    return true;
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelectMode = true;
                    if (isSelectMode){
                        if (selectItem.contains(users.get(getAdapterPosition()))){
                            userImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                            selectItem.remove(users.get(getAdapterPosition()));
                        }else {
                            userImageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
                            selectItem.add(users.get(getAdapterPosition()));
                            checkedPosition = getAdapterPosition();
                        }
                        if (selectItem.size() == 0 ){
                            isSelectMode = false;
                        }
                    }else {
                        if (checkedPosition!= getAdapterPosition()){
                            notifyItemChanged(checkedPosition);
                        }

                        FunctionAll functionAll = new FunctionAll();
                        functionAll.showToast(context,"else ");
                    }
                }
            });
        }

    }


}
