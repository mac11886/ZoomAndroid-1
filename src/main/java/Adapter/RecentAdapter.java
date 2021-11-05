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

import us.zoom.sdksample.Model.Team;
import us.zoom.sdksample.Model.ZoomHost;
import us.zoom.sdksample.Model.zoom_list;
import us.zoom.sdksample.R;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {
    List<ZoomHost> zoomHosts;
    Context context;
    private int checkedPosition = 0;
    boolean isSelectMode = false;
    List<ZoomHost> selectItem = new ArrayList<>();
    public RecentAdapter(Context context, List<ZoomHost> zoomHosts) {
        this.context = context;
        this.zoomHosts = zoomHosts;

    }
    public List<ZoomHost> getSelectItem() {
        if (selectItem != null) {
            return selectItem;
        }
        return null;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recently, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(zoomHosts.get(position));
        holder.hostText.setText(zoomHosts.get(position).getUser().getName());
        holder.dateText.setText(zoomHosts.get(position).getZoom_list().getDate());
    }

    @Override
    public int getItemCount() {
        return zoomHosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText,hostText;
        ImageView checkImage, iconUser;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateMeeting);
            checkImage = itemView.findViewById(R.id.checkImageViewRecent);
//            iconUser = itemView.findViewById(R.id.userImageView);
            hostText = itemView.findViewById(R.id.memberTeamTextview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelectMode = true;
                    if (isSelectMode) {
                        if (selectItem.contains(zoomHosts.get(getAdapterPosition()))) {
                            checkImage.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                            selectItem.remove(zoomHosts.get(getAdapterPosition()));
                        } else {
                            checkImage.setImageResource(R.drawable.ic_baseline_check_circle_24);
                            selectItem.add(zoomHosts.get(getAdapterPosition()));
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

        void bind(final ZoomHost zoomHost) {
            if (checkedPosition == -1) {
//               imageView.setImageDrawable(R.drawable.ic_baseline_check_circle_24);
                checkImage.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
//                    iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
            } else {
                if (checkedPosition == getAdapterPosition()) {
//                    imageView.setVisibility(View.VISIBLE);
//                    Toast.makeText(context,"choose",Toast.LENGTH_SHORT).show();
                    checkImage.setImageResource(R.drawable.ic_baseline_check_circle_24);
//                        iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
                } else {
                    checkImage.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
//                        iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
                }
            }
            hostText.setText(zoomHost.getUser().getName());
            dateText.setText(zoomHost.getZoom_list().getDate());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkImage.setImageResource(R.drawable.ic_baseline_check_circle_24);
//                        String email =  users.get(getAdapterPosition()).getEmail();
//                        String password =  users.get(getAdapterPosition()).getPassword();
//                    Toast.makeText(context,""+email + password,Toast.LENGTH_SHORT).show();

                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });

        }
    }
}
