package com.example.jazibhassan.thelibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SignUpActivity extends Activity {

    EditText e_userid,
            e_password,
            e_confirmpass,
            e_email,
            e_name,
            e_fname,
            e_address,
            e_contact,
            e_telno;

    Button btn_confirm;


    //For the other content view
    Button btn_chooseimage, btn_uploadimage, btn_skip;
    private int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap = null;
    private Uri filePath;

    public static final String UPLOAD_URL = MainActivity.IP + "library/upload.php";
    public static final String UPLOAD_KEY = "image";

    public static String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String userid = e_userid.getText().toString();
                String password = e_password.getText().toString();
                String confirmpass = e_confirmpass.getText().toString();
                String email = e_email.getText().toString();
                String name = e_name.getText().toString();
                String fname = e_fname.getText().toString();
                String address = e_address.getText().toString();
                String contact = e_contact.getText().toString();
                String tellno = e_telno.getText().toString();

                if (password.compareTo(confirmpass) == 0) {
                    btn_confirm.setEnabled(false);
                    new SignupTask().execute(userid, password, email, name, address, contact, tellno, fname);
                    user_id = userid;

                } else {
                    Toast.makeText(SignUpActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    void init() {
        e_userid = (EditText) findViewById(R.id.sedit1);
        e_password = (EditText) findViewById(R.id.sedit2);
        e_confirmpass = (EditText) findViewById(R.id.sedit3);
        e_email = (EditText) findViewById(R.id.sedit4);
        e_name = (EditText) findViewById(R.id.sedit5);
        e_address = (EditText) findViewById(R.id.sedit6);
        e_contact = (EditText) findViewById(R.id.sedit7);
        e_telno = (EditText) findViewById(R.id.sedit8);
        e_fname = (EditText) findViewById(R.id.sedit9);
        btn_confirm = (Button) findViewById(R.id.signup);
    }


    class SignupTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String sign_url = MainActivity.IP + "library/add_urequest.php";


            try {
                URL url = new URL(sign_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.setConnectTimeout(8000);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //new SignupTask().execute(userid,password,email,name,address,contact,tellno);
                String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&" +
                        URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8") + "&" +
                        URLEncoder.encode("tellno", "UTF-8") + "=" + URLEncoder.encode(params[6], "UTF-8") + "&" +
                        URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(params[7], "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int response_code = httpURLConnection.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    inputStream.close();

                    return "Success";

                }
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                return "Error";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Akhtar", "Signup Task Finished");
            btn_confirm.setEnabled(true);
            if (s.compareTo("Success") == 0) {
                setContentView(R.layout.activity_signup2);
                inti2();
                //finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    void inti2() {
        btn_chooseimage = (Button) findViewById(R.id.imagechoose);
        btn_uploadimage = (Button) findViewById(R.id.upload_image);
        btn_skip = (Button) findViewById(R.id.imageskip);
        imageView = (ImageView) findViewById(R.id.imageview);

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btn_uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignUpActivity.this, "Uploading Image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put("user_id", user_id);

                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        if(bitmap==null){
            Toast.makeText(this,"Please choose a photo",Toast.LENGTH_SHORT).show();
        }else{
            UploadImage ui = new UploadImage();
            ui.execute(bitmap);
        }

    }


}
