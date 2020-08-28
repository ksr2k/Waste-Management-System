
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

public class Worker extends AppCompatActivity implements View.OnClickListener {
    ImageButton ib1,ib2;
    String doneby="0";
    private String url = "jdbc:mysql://waste-management-database.cqkj1kgoaaet.us-east-1.rds.amazonaws.com:3306/wastemanagement";
    private String usr = "root";
    private String pwd = "password";
    String pid="";
    Connection con;
    Statement st1,st2,st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        ib1=findViewById(R.id.details);
        ib2=findViewById(R.id.service);
        ib1.setOnClickListener(this);
        ib2.setOnClickListener(this);
        ib1.setImageResource(R.drawable.details);
        ib2.setImageResource(R.drawable.service);
        Bundle b = getIntent().getExtras();
        pid = b.getString("id2");
    }

    @Override
    public void onClick(View view) {
        if(ib1.getId()==view.getId())
        {
            doneby="1";
            new myTask().execute();

        }
        else
        {
            doneby="2";
            new myTask().execute();
        }



    }
    public void showmessage(String s)
    {
        AlertDialog.Builder al =
                new AlertDialog.Builder(
                        Worker.this);
        if(doneby.equals("1"))
        {al.setTitle("Worker Details");
            al.setIcon(R.drawable.workers);}
        else
        {
            al.setTitle("Service Details");
            al.setIcon(R.drawable.service);
        }
        al.setMessage(s);
        al.setCancelable(true);


        al.show();

    }

    private class myTask extends AsyncTask<Void,Void,Void>
    { ProgressDialog prgDialog;
        String s="";
        String ultimate1="",ultimate="";
        int count=0;

        protected void onPreExecute()
        {
            super.onPreExecute();
            prgDialog = new ProgressDialog(
                    Worker.this);
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
                Connection con = DriverManager.getConnection(url, usr, pwd);
                st = con.createStatement();
                st1=con.createStatement();
                st2=con.createStatement();
                ResultSet rs1,rs2,rs3;

                if(doneby.equals("1"))
                {

                    rs1=st.executeQuery("Select * from Worker where WorkerID='"+pid+"' ;");
                    rs1.first();
                    String temp1=rs1.getString(4);

                    rs2=st1.executeQuery("Select * from WorkerDetails where Name='"+temp1+"' ;");

                    rs2.first();
                    ultimate+="--------------------------------------------------\n";
                    ultimate+="Worker ID: "+pid+"\n";
                    ultimate+="--------------------------------------------------\n";
                    ultimate+="Name :"+rs2.getString(1)+"\n";
                    ultimate+="--------------------------------------------------\n";
                    ultimate+="Place :"+rs2.getString(2)+"\n";
                    ultimate+="--------------------------------------------------\n";
                    ultimate+="Contact :"+rs2.getString(3)+"\n";
                    ultimate+="--------------------------------------------------\n";

                    ultimate+="Salary :"+rs2.getString(5)+"\n";
                    ultimate+="--------------------------------------------------\n";


                }
                else
                {
                    rs1=st.executeQuery("select * from Worker where WorkerID = '"+pid+"' ;");
                    rs1.first();
                    String temp1=rs1.getString(2);
                    String temp2=rs1.getString(3);
                    rs2=st1.executeQuery("select * from Admin where AdminID = '"+temp1+"' ;");
                    rs2.first();
                    String manager=rs2.getString(2);
                    if(temp2.charAt(0)=='P')
                    {
                        rs3=st2.executeQuery("Select * from PublicServices where ServiceID='"+temp2+"' ;");}
                    else
                    {
                        rs3=st2.executeQuery("Select * from CompanyServices where ServiceID='"+temp2+"' ;");
                    }
                    rs3.first();
                    String name=rs3.getString(2);
                    ultimate1+="--------------------------------------------------\n";
                    ultimate1+="Service Managed by:"+manager+"\n";
                    ultimate1+="--------------------------------------------------\n";
                    ultimate1+="Service Allocated to:"+name+"\n";
                    ultimate1+="--------------------------------------------------\n";

                }


                con.close();
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
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            if(doneby.equals("1"))
            showmessage(ultimate);
            else
            {
                showmessage(ultimate1);
            }



        }
    }
}
