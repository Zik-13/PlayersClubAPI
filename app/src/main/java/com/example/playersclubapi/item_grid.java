package com.example.playersclubapi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class item_grid extends AppCompatActivity {
    Integer id;
    EditText name, phone, email;
    ImageView image;
    Bitmap img;


    Intent Mainactiv;

    String stringImage;
    String encodedImage = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_grid);

        Mainactiv = new Intent(this,MainActivity.class);

        Bundle arguments = getIntent().getExtras();
        String _name = arguments.get("name").toString();
        String _phone = arguments.get("phone").toString();
        String _email = arguments.get("email").toString();

        String _image;
        try {
            _image = arguments.get("image").toString();
        }
        catch (Exception e){
            _image = "";
        }
        id = Integer.valueOf(arguments.get("id").toString());


        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        image = findViewById(R.id.image);

        name.setText(_name);
        phone.setText(_phone);
        email.setText(_email);

        if(_image != ""){
            image.setImageBitmap(getImageBitmap(_image));
        }
        else {
            image.setImageResource(R.drawable.ic_baseline_person_24);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delet_item){
            DeleteItemDialog();
        }
        if (id == R.id.back_item){
            back();
        }
        if (id == R.id.save_item){
            UpdateItemDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void ImageClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImg.launch(intent);
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    image.setImageBitmap(bitmap);
                    encodedImage = encodeImage(bitmap);
                } catch (Exception e) {

                }
            }
        }
    });
    private String encodeImage(Bitmap bitmap) {
        int prevW = 150;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }

    private Bitmap getImageBitmap(String encodedImg) {
        if (encodedImg != null) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(item_grid.this.getResources(),
                R.drawable.ic_baseline_person_24);
    }

    public void btn_onclick_back_main(View view){this.finish();}


    private void putData(String name, String phone, String email, String image) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl
                        ("https://ngknn.ru:5001/NGKNN/ЛебедевДВ/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Staff staff = new Staff(id.toString(),name, phone, email, image);

        Call<Staff> call = retrofitAPI.updateData(id, staff);

        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(@NonNull Call<Staff> call, @NonNull Response<Staff> response) {
                Toast.makeText(item_grid.this, "Club member details updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Staff> call, @NonNull Throwable t) {
                Toast.makeText(item_grid.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void back(){startActivity(Mainactiv);}
    public void UpdateItemDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Update info?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        putData(name.getText().toString(), phone.getText().toString(), email.getText().toString(), encodedImage);
                        back();
                    }
                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }

    public void DeleteItemDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Remove club member?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://ngknn.ru:5001/NGKNN/ЛебедевДВ/api/")

                                .addConverterFactory(GsonConverterFactory.create()).build();
                        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
                        Call<Staff> call = retrofitAPI.deleteData(id);
                        call.enqueue(new Callback<Staff>() {
                            @Override
                            public void onResponse(@NonNull Call<Staff> call, @NonNull Response<Staff> response) {
                                Toast.makeText(item_grid.this, "Club member removed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Staff> call, @NonNull Throwable t) {
                                Toast.makeText(item_grid.this, t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        back();
                    }
                })
                .setNegativeButton("No",null)
                .create()
                .show();
    }
}