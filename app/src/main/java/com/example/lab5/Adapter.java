package com.example.lab5;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHoder> {

    private final List<Distributors> list;
    private final Context context;
    Distributors distributors;

    public Adapter(List<Distributors> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.item,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
            holder.id.setText("ID: "+list.get(position).getId());
            holder.name.setText("Name: "+list.get(position).getName());
            String idd=list.get(position).getId();
        holder.btnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận")
                        .setMessage("Bạn có muốn xóa không ?");
                // Thiết lập nút Yes và xử lý sự kiện khi người dùng chọn Yes
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Lấy id của trái cây cần xóa từ list
                        String distributorId = list.get(holder.getAdapterPosition()).getId();

                        Log.d("iddd", "onClick: "+distributorId);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiService.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        ApiService apiService = retrofit.create(ApiService.class);
                        Call<Distributors> call = apiService.deleteDistributorsById(distributorId);
                        call.enqueue(new Callback<Distributors>() {
                            @Override
                            public void onResponse(Call<Distributors> call, Response<Distributors> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    list.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Distributors> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                Log.e("City_Adapter", "Error deleting sinh vien", t);
                            }
                        });
                    }
                });

                // Thiết lập nút No và xử lý sự kiện khi người dùng chọn No
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng chọn No
                        // Ví dụ: Đóng hộp thoại hoặc không làm gì cả
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        // update

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.itemupdate, null);
                builder.setView(view);
                Dialog dialog = builder.create();
                dialog.show();

                EditText edtname = view.findViewById(R.id.name);
                Button btnsua = view.findViewById(R.id.btnthem);
                edtname.setText(list.get(position).getName());

                btnsua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Sửa");
                        builder.setMessage("Bạn có muốn sửa Sinh Viên không?");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = list.get(position).getId();
                                String ten = edtname.getText().toString().trim();

                                // Kiểm tra các trường thông tin có được nhập đầy đủ hay không
                                if (ten.isEmpty()) {
                                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Tạo một đối tượng SinhVien mới
                                Distributors dink = new Distributors(id,ten);

                                // Gửi đối tượng SinhVien cập nhật lên server
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(ApiService.BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                ApiService apiService = retrofit.create(ApiService.class);
                                Call<Distributors> call = apiService.updateDink(id, dink);
                                call.enqueue(new Callback<Distributors>() {
                                    @Override
                                    public void onResponse(Call<Distributors> call, Response<Distributors> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            list.get(position).setName(ten);

                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Distributors> call, Throwable t) {
                                        Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                        Log.e("City_Adapter", "Error updating sinh vien", t);

                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {
        TextView id,name;
        ImageView btnx;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.TvID);
            name=itemView.findViewById(R.id.TvName);
            btnx=itemView.findViewById(R.id.BtnDelete);
        }
    }
}
