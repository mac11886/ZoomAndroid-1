package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.R;

public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.SingleViewHolder> {


    public SingleAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;

    }

    private Context context;
    private ArrayList<User> users;
    private int checkedPosition = 0 ;

    public void setUsers(ArrayList<User> users){
        this.users = new ArrayList<>();
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return  new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SingleViewHolder holder, int position) {
        holder.bind(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class SingleViewHolder extends  RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;


        public SingleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.user_name);
            imageView = itemView.findViewById(R.id.checkImageView)
;        }

        void  bind(final User user){
            if (checkedPosition == -1){
//               imageView.setImageDrawable(R.drawable.ic_baseline_check_circle_24);
                imageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            }else {
                if(checkedPosition == getAdapterPosition()){
//                    imageView.setVisibility(View.VISIBLE);
//                    Toast.makeText(context,"choose",Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
                }
                else {
                    imageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                }
            }

            textView.setText(user.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
                     if (checkedPosition!= getAdapterPosition()){
                         notifyItemChanged(checkedPosition);
                         checkedPosition = getAdapterPosition();
                     }
                }
            });
        }
    }


    public User getSelected(){
        if(checkedPosition!=-1){
            return users.get(checkedPosition);
        }
        return null;
    }

}
