package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.R;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<User> users;
    Context context;
    private int checkedPosition = 0 ;
    public UserAdapter(Context context,List<User> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(users.get(position));


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
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        ImageView userImageView,iconUser;

        public ViewHolder(@NonNull View view) {
            super(view);
            nameText = view.findViewById(R.id.user_name);
            userImageView =view.findViewById(R.id.checkImageView);
            iconUser = view.findViewById(R.id.userImageView);
        }
        void  bind(final User user){
            if (checkedPosition == -1){
//               imageView.setImageDrawable(R.drawable.ic_baseline_check_circle_24);
                userImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
            }else {
                if(checkedPosition == getAdapterPosition()){
//                    imageView.setVisibility(View.VISIBLE);
//                    Toast.makeText(context,"choose",Toast.LENGTH_SHORT).show();
                    userImageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
                }
                else {
                    userImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                    iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
                }
            }

            nameText.setText(user.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userImageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    String email =  users.get(getAdapterPosition()).getEmail();
                    String password =  users.get(getAdapterPosition()).getPassword();
//                    Toast.makeText(context,""+email + password,Toast.LENGTH_SHORT).show();

                    if (checkedPosition!= getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }

}
