package com.example.playersclubapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private AdaptStaff sAdapter;
    private List<Staff> staffList = new ArrayList<>();

    ListView list;
    Intent add_new_staff;
    Intent item_gridd;
    AdaptStaff adapter;
    Spinner sort;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView ivProducts = findViewById(R.id.gridviewlist);
        sAdapter = new AdaptStaff(MainActivity.this, (ArrayList<Staff>) staffList);
        ivProducts.setAdapter(sAdapter);

        new GetProducts().execute();



        String[] sort_name = { "Name", "Email", "Phone"};
        sort = findViewById(R.id.sort);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sort_name);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort.setAdapter(spinner_adapter);

        list = findViewById(R.id.gridviewlist);
        item_gridd = new Intent(this,item_grid.class);
        add_new_staff = new Intent(this, com.example.playersclubapi.add_new_staff.class);


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 );


        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Collections.sort(staffList, new Comparator<Staff>() {
                            @Override
                            public int compare(Staff staff, Staff t1) {
                                return staff.name.compareTo(t1.name);
                            }
                        });
                        sAdapter = new AdaptStaff(MainActivity.this, (ArrayList<Staff>) staffList);
                        list.setAdapter(sAdapter);
                        break;
                    case 1:
                        Collections.sort(staffList, new Comparator<Staff>() {
                            @Override
                            public int compare(Staff staff, Staff t1) {
                                return staff.email.compareTo(t1.email);
                            }
                        });
                        sAdapter = new AdaptStaff(MainActivity.this, (ArrayList<Staff>) staffList);
                        list.setAdapter(sAdapter);
                        break;
                    case 2:
                        Collections.sort(staffList, new Comparator<Staff>() {
                            @Override
                            public int compare(Staff staff, Staff t1) {
                                return staff.phone.compareTo(t1.phone);
                            }
                        });
                        sAdapter = new AdaptStaff(MainActivity.this, (ArrayList<Staff>) staffList);
                        list.setAdapter(sAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Staff item = (Staff) list.getItemAtPosition(i);

                item_gridd.putExtra("name", item.name);
                item_gridd.putExtra("id",item.id);
                item_gridd.putExtra("email",item.phone);
                item_gridd.putExtra("phone",item.email);
                if(item.imageB != null) {
                    item_gridd.putExtra("image",encodeImage(item.imageB));
                }
                startActivity(item_gridd);
            }
        }
        );

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItemsearch = menu.findItem(R.id.action_search);
        SearchView  searchView = (SearchView) menuItemsearch.getActionView();
        searchView.setQueryHint("Type");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add){
            startActivity(add_new_staff);
        }
        if (id == R.id.action_update){
            staffList.clear();
            new GetProducts().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onclick_view_add_staff(View view){startActivity(add_new_staff);}
    public void UpdateList(View view){
        staffList.clear();
        new GetProducts().execute();
    }


private String encodeImage(Bitmap bitmap) {
    int prevW = 150;
    int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
    Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
    byte[] bytes = byteArrayOutputStream.toByteArray();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    return "";
}



    private class GetProducts extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/ЛебедевДВ/api/Staffapis");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();

            } catch (Exception exception) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0;i<tempArray.length();i++)
                {

                    JSONObject jsonObject = tempArray.getJSONObject(i);
                    Staff staff = new Staff(
                            jsonObject.getString("name"),
                            jsonObject.getString("phone"),
                            jsonObject.getString("email"),
                            jsonObject.getString("id"),

                            getImageBitmap(jsonObject.getString("image")));
                    staffList.add(staff);
                    sAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ignored) {


            }
        }
        private Bitmap getImageBitmap(String encodedImg) {
            if (encodedImg != null) {
                byte[] bytes = new byte[0];
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    bytes = Base64.getDecoder().decode(encodedImg);
                }
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            return BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher_foreground);
        }
    }
}