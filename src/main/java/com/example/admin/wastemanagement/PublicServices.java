package com.example.admin.wastemanagement;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PublicServices extends AppCompatActivity implements View.OnClickListener {
    Connection con;
    Statement st;
    EditText e1, e2,e3;
    Button b1;

    private String url="jdbc:mysql://waste-management-database.cqkj1kgoaaet.us-east-1.rds.amazonaws.com:3306/wastemanagement";
    private String usr="root";
    private String pwd="password";
    String s1,s2,s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_services);
        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3=findViewById(R.id.e3);
        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        s1=e1.getText().toString();
        s2=e2.getText().toString();
        s3=e3.getText().toString();
        new myTask().execute();
    }

    private class myTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog prgDialog;
        protected void onPreExecute()
        {
            super.onPreExecute();
            prgDialog = new ProgressDialog(
                    PublicServices.this);
            prgDialog.setMessage
                    ("Requesting services");
            prgDialog.setIndeterminate(false);
            prgDialog.setProgressStyle
                    (ProgressDialog.STYLE_SPINNER);
            prgDialog.setCancelable(false);
            prgDialog.show();




        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, usr, pwd);
                st = con.createStatement();
                int rs= st.executeUpdate("insert into PublicServices values ('"+s1+"'"+",'"+s2+"');");
                rs= st.executeUpdate("insert into CostOfServices values ('"+s1+"'"+",'"+s3+"');");
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;}
        @Override
        protected void onPostExecute(Void result)
        {

            super.onPostExecute(result);
            prgDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Requested service has been approved",Toast.LENGTH_LONG).show();


        }
    }
}

