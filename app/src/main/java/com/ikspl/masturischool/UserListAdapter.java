package com.ikspl.masturischool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    Context context;
    ArrayList<UserListModel> arrayList;

    public UserListAdapter(Context context,ArrayList<UserListModel> arraylist)
    {
        this.context = context;
        this.arrayList = arraylist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view= LayoutInflater.from(context).inflate(R.layout.userlistadapter,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        if(arrayList.get(i).getPic() == null)
        {
            myViewHolder.stdname.setText("Name :- "+arrayList.get(i).getStdname());
            myViewHolder.stdfathername.setText("Father's Name :- "+arrayList.get(i).getStdfathername());
            myViewHolder.stdmothername.setText("Mother's Name :- "+arrayList.get(i).getStdmothername());
            myViewHolder.cvl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ii = new Intent(context,WebPages.class);
                    ii.putExtra("StuID", "LOGIN");
                    ii.putExtra("SNo", arrayList.get(i).getStdadmisionid());
                    context.startActivity(ii);
                }
            });
        }
        else
        {
            int blobLength = 0;
            try {
                blobLength = (int) arrayList.get(i).getPic().length();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            byte[] blobAsBytes = new byte[0];
            try {
                blobAsBytes = arrayList.get(i).getPic().getBytes(1, blobLength);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);
            myViewHolder.stdpic.setImageBitmap(btm);

            myViewHolder.stdname.setText("Name :- "+arrayList.get(i).getStdname());
            myViewHolder.stdfathername.setText("Father's Name :- "+arrayList.get(i).getStdfathername());
            myViewHolder.stdmothername.setText("Mother's Name :- "+arrayList.get(i).getStdmothername());
            myViewHolder.cvl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ii = new Intent(context,WebPages.class);
                    ii.putExtra("StuID", "LOGIN");
                    ii.putExtra("SNo", arrayList.get(i).getStdadmisionid());
                    context.startActivity(ii);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView stdpic;
        TextView stdname,stdfathername,stdmothername;
        CardView cvl;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cvl = itemView.findViewById(R.id.cardviewlist);
            stdpic = itemView.findViewById(R.id.imgpic);
            stdname = itemView.findViewById(R.id.txtstdname);
            stdfathername = itemView.findViewById(R.id.txtstdfather);
            stdmothername = itemView.findViewById(R.id.txtstdmother);
        }
    }
}
