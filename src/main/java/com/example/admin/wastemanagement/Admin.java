package com.example.admin.wastemanagement;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Admin extends AppCompatActivity implements View.OnClickListener {
    ImageButton ib1,ib2,ib3;
    String doneby="";
    Connection con;
    Statement st;
    private String url = "jdbc:mysql://waste-management-database.cqkj1kgoaaet.us-east-1.rds.amazonaws.com:3306/wastemanagement";
    private String usr = "root";
    private String pwd = "password";
    String ultimate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ib1=findViewById(R.id.viewpublic);
        ib2=findViewById(R.id.viewworkers);
        ib3=findViewById(R.id.ViewCompanies);

        ib1.setImageResource(R.drawable.download);
        ib2.setImageResource(R.drawable.workers);
        ib3.setImageResource(R.drawable.company);

        ib1.setOnClickListener(this);
        ib2.setOnClickListener(this);
        ib3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(ib1.getId()==view.getId())
        {
            doneby="P";
            new myTask().execute();
        }
        else if(ib2.getId()==view.getId())
        {
            doneby="W";
            new myTask().execute();
        }
        else
        {
            doneby="C";
            new myTask().execute();
        }
    }
    public void showmessage(String s)
    {
        AlertDialog.Builder al =
                new AlertDialog.Builder(
                        Admin.this);
        if(doneby.equals("C"))
        {al.setTitle("Company Details");
        al.setIcon(R.drawable.company);}
        else if(doneby.equals("W"))
        {al.setTitle("Worker Details");
            al.setIcon(R.drawable.workers);}
         else
        {
            al.setTitle("Public Details");
            al.setIcon(R.drawable.download);
        }
        al.setMessage(s);
        al.setCancelable(true);


        al.show();

    }
    private class myTask extends AsyncTask<Void,Void,Void>
    { ProgressDialog prgDialog;
        String s="";

        protected void onPreExecute()
        {
            super.onPreExecute();
            prgDialog = new ProgressDialog(
                    Admin.this);
            prgDialog.setMessage
                    ("Gathering Details");
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
                ResultSet rs;
                ultimate="";
                if(doneby.equals("C"))
                {
                    rs=st.executeQuery("Select * from CompanyDetails");

                    ultimate+="--------------------------------------------------\n";
                    while(rs.next())
                    {
                        ultimate+="Name: "+rs.getString(1)+"\n"+"Street: "+rs.getString(2)+"\nCity: "+rs.getString(3)+"\nZipcode: "+rs.getString(4)+"\n Phone: ";
                        ultimate+="--------------------------------------------------\n";
                    }
                }
                else if(doneby.equals("W"))
                {
                    rs=st.executeQuery("Select * from WorkerDetails");
                    ultimate+="--------------------------------------------------\n";
                    while(rs.next())
                    {
                        ultimate+="Name: "+rs.getString(1)+"\nPlace: "+rs.getString(2)+"\nContact: "+rs.getString(3)+"\n DOB: "+rs.getString(4)+"\nSalary: "+rs.getString(5)+"\n";
                        ultimate+="--------------------------------------------------\n";
                    }
                }
                else
                {
                    rs=st.executeQuery("Select * from PublicDetails");
                    ultimate+="--------------------------------------------------\n";
                    while(rs.next())
                    {
                        ultimate+="Name: "+rs.getString(1)+"\nPlace: "+rs.getString(2)+"\nDoor no: "+rs.getString(3)+"\nPhone"+rs.getString(4)+"\n";
                        ultimate+="--------------------------------------------------\n";
                    }
                }
            }
            catch (Exception E)
            {

                E.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {

            super.onPostExecute(result);
            prgDialog.dismiss();
            showmessage(ultimate);



        }
    }}