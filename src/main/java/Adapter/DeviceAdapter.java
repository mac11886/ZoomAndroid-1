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

import java.util.List;

import us.zoom.sdksample.Model.Device;
import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.R;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>  {

    List<Device> devices;
    Context context;
    private int checkedPosition = 0 ;

    public DeviceAdapter(Context context,List<Device> devices){
        this.context = context;
        this.devices = devices;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_device, parent, false);
        return new DeviceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(devices.get(position));
    }
    public Device getSelected(){
        if(checkedPosition!=-1){
            return devices.get(checkedPosition);
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView deviceText,deviceLocation;
        ImageView deviceImageView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            deviceText =itemView.findViewById(R.id.deviceName);
            deviceImageView = itemView.findViewById(R.id.checkDeviceImageView);
            deviceLocation = itemView.findViewById(R.id.deviceLocation);
        }
        void  bind(final Device device){
            if (checkedPosition == -1){
//               imageView.setImageDrawable(R.drawable.ic_baseline_check_circle_24);
                deviceImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
//                iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
            }else {
                if(checkedPosition == getAdapterPosition()){
//                    imageView.setVisibility(View.VISIBLE);
//                    Toast.makeText(context,"choose",Toast.LENGTH_SHORT).show();
                    deviceImageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
//                    iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
                }
                else {
                    deviceImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
//                    iconUser.setImageResource(R.drawable.zm_menu_icon_add_contact);
                }
            }
            deviceLocation.setText("@"+device.getLocation());
            deviceText.setText(device.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceImageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
//                    String email =  devices.get(getAdapterPosition()).getEmail();
//                    String password =  devices.get(getAdapterPosition()).getPassword();
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
