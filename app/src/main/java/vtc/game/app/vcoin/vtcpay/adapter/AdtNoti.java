package vtc.game.app.vcoin.vtcpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import vtc.game.app.vcoin.vtcpay.R;
import vtc.game.app.vcoin.vtcpay.common.AppBase;
import vtc.game.app.vcoin.vtcpay.model.NotiItem;

/**
 * Created by ThuyChi on 6/22/2016.
 */
public class AdtNoti extends ArrayAdapter<NotiItem> {
    Context mContext;
    ArrayList<NotiItem> mLstEmailItem = new ArrayList<NotiItem>();
    private onClickItem onClickItem;
    private SwipeLayout lastSwipelayout;

    public AdtNoti(Context context, int resource, List<NotiItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mLstEmailItem = new ArrayList<NotiItem>(objects);
    }

    @Override
    public int getCount() {
        if (mLstEmailItem == null) {
            return 0;
        }
        return mLstEmailItem.size();
    }

    @Override
    public NotiItem getItem(int position) {
        if (mLstEmailItem == null) {
            return null;
        }
        return mLstEmailItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return initView(position, convertView, parent);
    }

    public View initView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final ViewHolder viewHolder;
        if (rowView == null) {
            LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflate.inflate(R.layout.tmp_item_noti, null);

            viewHolder = new ViewHolder();
            viewHolder.loutItem = (LinearLayout) rowView.findViewById(R.id.lout_Content_Email);
            viewHolder.loutDelete = (RelativeLayout) rowView.findViewById(R.id.right_Wrapper);
            viewHolder.tvTitile = (TextView) rowView.findViewById(R.id.tv_Game_Titile);
            viewHolder.swipeLayout = (SwipeLayout) rowView.findViewById(R.id.swipeLayoutSimple);
            viewHolder.imgBanerNoti = (ImageView) rowView.findViewById(R.id.img_Baner_Noti);
            viewHolder.imgIsOpen = (ImageView) rowView.findViewById(R.id.img_Check_Read_Noti);
            viewHolder.tvTime = (TextView) rowView.findViewById(R.id.tv_Time);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.loutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppBase.showLog("Click detele button:" + position);
//                getOnClickItem().onClickItem(1, position);
                getOnClickItem().onDeleteItem(getItem(position).getNotiID(), getItem(position));
                mLstEmailItem.remove(position);
                notifyDataSetChanged();
                viewHolder.swipeLayout.close();
            }
        });


        viewHolder.loutItem.setOnClickListener(new View.OnClickListener() {
            int pos = position;

            @Override
            public void onClick(View v) {
                AppBase.showLog("onClick:" + pos);
                getOnClickItem().onClickItem(getItem(position).getNotiID(), getItem(position));
                getItem(position).setOpen(true);
                notifyDataSetChanged();
            }
        });
        final NotiItem item = getItem(position);

        viewHolder.tvTitile.setText(item.getNotiTitle());
        AppBase.setImageCircular(item.getPictureUrl(), viewHolder.imgBanerNoti);

        if (item.isOpen) {
            viewHolder.imgIsOpen.setVisibility(View.GONE);
        } else {
            viewHolder.imgIsOpen.setVisibility(View.VISIBLE);
        }

        long currentTime = System.currentTimeMillis();
        long diff = currentTime - getItem(position).getCreateTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        long s = diff % 60;
        long m = (diff / 60) % 60;
        long h = (diff / (60 * 60)) % 24;
        long d = ((diff / (60 * 60)) % 24) / 24;
        String time = "";

//        if (d > 0) {
//            time = d + " ngày " + h + " giờ " + m + " phút " + s + " giây phút trước";
//        } else if ((h > 0) && (d == 0)) {
//            time = h + " giờ " + m + " phút " + s + " giây phút trước";
//        } else if ((h == 0) && (d == 0)) {
//            time = m + " phút " + s + " giây phút trước";
//        } else if ((h == 0) && (d == 0) && (m == 0)) {
//            time = s + " giây phút trước";
//        }

//        if (days > 0) {
//            time = days + " ngày " + (hours % 24) + " giờ " + (minutes % 60) + " phút " + (seconds % 60) + " giây phút trước";
//        } else if (((hours % 24) > 0) && (days == 0)) {
//            time = hours + " giờ " + (minutes % 60) + " phút " + (seconds % 60) + " giây phút trước";
//        } else if ((h == 0) && (days == 0)) {
//            time = (minutes % 60) + " phút " + (seconds % 60) + " giây phút trước";
//        } else if (((hours % 24) == 0) && (days == 0) && ((minutes % 60) == 0)) {
//            time = (seconds % 60) + " giây phút trước";
//        }

        if (days > 5) {
            time = "Nhiều ngày trước";
        } else if ((days >= 2) && (days <= 5)) {
            time = days + " ngày trước";
        } else if ((days > 0) && (days < 2)) {
            time = days + " ngày " + (hours % 24) + " giờ " + (minutes % 60) + " phút trước";
        } else if (((hours % 24) > 0) && (days == 0)) {
            time = hours + " giờ " + (minutes % 60) + " phút trước";
        } else if (((hours % 24) == 0) && (days == 0) && ((minutes % 60) != 0)) {
            time = (minutes % 60) + " phút trước";
        } else if (((hours % 24) == 0) && (days == 0) && ((minutes % 60) == 0)) {
            time = "Vừa xong";
        }

//        time = days + " ngày " + (hours % 24) + " giờ " + (minutes % 60) + " phút " + (seconds % 60) + " giây phút trước";
        viewHolder.tvTime.setText(time);

        return rowView;

    }

    static class ViewHolder {
        LinearLayout loutItem;
        RelativeLayout loutDelete;
        TextView tvTitile;
        SwipeLayout swipeLayout;
        ImageView imgBanerNoti;
        ImageView imgIsOpen;
        TextView tvTime;
    }

    public interface onClickItem {
        void onClickItem(String notiID, NotiItem item);

        void onDeleteItem(String notiID, NotiItem item);
    }

    public AdtNoti.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtNoti.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }
}