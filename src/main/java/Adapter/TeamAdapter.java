package Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import us.zoom.sdksample.Model.Team;
import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.R;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    Context context;
    //init
    private Activity activity;
    List<Team> arrayListGroup;
    boolean isSelectMode = false;
    private int checkedPosition = 0;
    List<Team> selectItem = new ArrayList<>();

    //constructor
    public TeamAdapter(Activity activity, List<Team> arrayListGroup) {
        this.activity = activity;
        this.arrayListGroup = arrayListGroup;
    }

    public List<Team> getSelectItem() {
        if (selectItem != null) {
            return selectItem;
        }
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_group, parent, false);
        return new TeamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(arrayListGroup.get(position).getName());
        String name = "";
        for (User user : arrayListGroup.get(position).getUser()) {
            name += user.getName();
            name += ",";

        }
        name = name.substring(0,name.length() - 1);
        holder.memberText.setText(name);

        //init member
//        ArrayList<String> arrayListMember = new ArrayList<>();
//        //using for loop to add multiple member
//        for (int i = 1; i <= 4; i++) {
//            arrayListMember.add("member" + i);
//        }

//        MemberAdapter memberAdapter = new MemberAdapter(arrayListMember);
//        //init layout manager
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
//        //set layout manager
//        holder.rvMember.setLayoutManager(linearLayoutManager);
//        //set adapter
//        holder.rvMember.setAdapter(memberAdapter);

    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //init
        TextView tvName;
        TextView memberText;
        ImageView checkImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkImage = itemView.findViewById(R.id.checkTeamImageView);
            tvName = itemView.findViewById(R.id.tv_name);
            memberText = itemView.findViewById(R.id.memberTeamTextview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelectMode = true;
                    if (isSelectMode) {
                        if (selectItem.contains(arrayListGroup.get(getAdapterPosition()))) {
                            checkImage.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                            selectItem.remove(arrayListGroup.get(getAdapterPosition()));
                        } else {
                            checkImage.setImageResource(R.drawable.ic_baseline_check_circle_24);
                            selectItem.add(arrayListGroup.get(getAdapterPosition()));
                            checkedPosition = getAdapterPosition();
                        }
                        if (selectItem.size() == 0) {
                            isSelectMode = false;
                        }
                    } else {
                        if (checkedPosition != getAdapterPosition()) {
                            notifyItemChanged(checkedPosition);
                        }

                        FunctionAll functionAll = new FunctionAll();
                        functionAll.showToast(context, "else ");
                    }
                }

            });
        }
    }
}
