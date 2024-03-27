package com.example.lab5;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Adapter adapter;
    private RecyclerView rcv;
    private List<Distributors> list;
    private FloatingActionButton add;
    TextInputEditText edTimkiem;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rcv=findViewById(R.id.rcv);
        add=findViewById(R.id.btnAdd);
        edTimkiem=findViewById(R.id.edtSeachr);
        edTimkiem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = edTimkiem.getText().toString().trim();
                    searchDrink(key);
                    return true;
                }
                return false;
            }


        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Them();
            }
        });
        getDrinkList();

//        rcv.post(new Runnable() {
//            @Override
//            public void run() {
//                // Kiểm tra xem RecyclerView đã sẵn sàng chưa
//                if (rcv.getAdapter() == null) {
//                    ApiService apiService = ApiClient.getApiService();
//                    Call<List<Distributors>> call = apiService.getData();
//                    call.enqueue(new Callback<List<Distributors>>() {
//                        @Override
//                        public void onResponse(Call<List<Distributors>> call, Response<List<Distributors>> response) {
//                            if (response.isSuccessful()) {
//                                List<Distributors> dataList = response.body();
//                                Log.d("Danh sách", "hhhhhh" +dataList);
//                                // Hiển thị dữ liệu lên RecyclerView
//                                // (Bạn cần một Adapter và LayoutManager cho RecyclerView)
//                                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//                                rcv.setLayoutManager(layoutManager);
//                                adapter= new Adapter(dataList,MainActivity.this);
//                                rcv.setAdapter(adapter);
//                                Log.d("Kết nối thành công api", "onResponse:");
//                            } else {
//                                // Xử lý lỗi
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<Distributors>> call, Throwable t) {
//                            // Xử lý lỗi kết nối
//                            Toast.makeText(MainActivity.this, "Kết nối thất bại", Toast.LENGTH_SHORT).show();
//                            Log.d(">>>>> lỗi", "onFailure: "+t.getMessage());
//                        }
//                    });
//                }
//            }
//        });

    }

    private void getDrinkList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Distributors>> call = apiService.getData();

        call.enqueue(new Callback<List<Distributors>>() {
            @Override
            public void onResponse(Call<List<Distributors>> call, Response<List<Distributors>> response) {
                if (response.isSuccessful()) {
                   list = response.body();
                    adapter = new Adapter(list,MainActivity.this);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    rcv.setLayoutManager(linearLayoutManager);
                    rcv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Distributors>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });
    }

    public void Them(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.itemadd, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtname = view.findViewById(R.id.name);
        Button btnthemm = view.findViewById(R.id.btnthem);

        btnthemm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Thêm");
                builder.setMessage("Bạn có muốn thêm Danh Sách không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ten = edtname.getText().toString().trim();


                        // Kiểm tra các trường thông tin có được nhập đầy đủ hay không
                        if (ten.isEmpty() ) {
                            Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Tạo một đối tượng SinhVien mới
                        Distributors dinks = new Distributors(ten);

                        // Gửi đối tượng SinhVien lên server
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiService.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        ApiService apiService = retrofit.create(ApiService.class);
                        Call<List<Distributors>> call = apiService.addDink(dinks);
                        call.enqueue(new Callback<List<Distributors>>() {
                            @Override
                            public void onResponse(Call<List<Distributors>> call, Response<List<Distributors>> response) {
                                if (response.isSuccessful()) {
                                    getDrinkList();
                                    Log.e("thanhcong","jdsbfdsgfdg");

                                } else {
                                    Log.e("Thatbai","aaaaaaaaaaaaaa");
                                    Log.e("sinhvien",dinks.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<java.util.List<Distributors>> call, Throwable t) {
                                getDrinkList();

                                dialog.dismiss();
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

    private void searchDrink(String key) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        // ... other code ...

        Call<ApiResponse> call = apiService.searchDink(key);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    list = response.body().getData();
                    adapter = new Adapter(list,MainActivity.this);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    rcv.setLayoutManager(linearLayoutManager);
                    rcv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });


    }
}