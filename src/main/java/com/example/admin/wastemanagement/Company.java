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

public class Company extends AppCompatActivity implements View.OnClickListener {

    ImageButton ib1,ib2;
    String doneby="0",fbbill="";
    int fcost=0;
    Connection con;
    Statement st;
    String pid="";
    private String url = "jdbc:mysql://waste-management-database.cqkj1kgoaaet.us-east-1.rds.amazonaws.com:3306/wastemanagement";
    private String usr = "root";
    private String pwd = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        ib1=findViewById(R.id.ib1);
        ib2=findViewById(R.id.ib2);
        ib1.setOnClickListener(this);
        ib2.setOnClickListener(this);
        ib1.setImageResource(R.drawable.companyservices);
        ib2.setImageResource(R.drawable.bill);
        Bundle b = getIntent().getExtras();
        pid = b.getString("id1");



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
                        Company.this);
        if(doneby.equals("1"))
        {al.setTitle("Company Services");
            al.setIcon(R.drawable.companyservices);}
        else
        {
            al.setTitle("Monthly bill");
            al.setIcon(R.drawable.bill);
        }
        al.setMessage(s);
        al.setCancelable(true);


        al.show();

    }

    private class myTask extends AsyncTask<Void,Void,Void>
    { ProgressDialog prgDialog;
        String s="";
        String ultimate="";
        int count=0;

        protected void onPreExecute()
        {
            super.onPreExecute();
            prgDialog = new ProgressDialog(
                    Company.this);
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
                Statement st1=con.createStatement();
                Statement st2=con.createStatement();

                ResultSet rs,rs1,rs2;
                if(doneby.equals("1"))
                {
                    rs=st.executeQuery("Select * from CompanyServices");

                    ultimate+="--------------------------------------------------\n";
                    while(rs.next())
                    {
                        //Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
                        String temp1=rs.getString(1);
                        String temp2=rs.getString(2);
                        ultimate+="Service id: "+temp1+"\n"+"Service name: "+temp2+"\n";
                        //Toast.makeText(getApplicationContext(),temp1,Toast.LENGTH_LONG).show();
                        rs1=st1.executeQuery("Select * from CostOfServices where ServiceID='"+temp1+"' ;");
                        //Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
                        rs1.first();
                        temp1=rs1.getString(2);
                        ultimate+="Cost:"+temp1+"\n";
                        ultimate+="--------------------------------------------------\n";

                    }
                }
                else
                {
                    rs = st.executeQuery("Select * from ServicesRequestedByCompany where CompanyID='"+pid+"' ;");
                    count=0;
                    while(rs.next())
                    {//count++;
                        String sid=rs.getString(2);
                        rs1=st1.executeQuery("Select * from CompanyServices where ServiceID='"+sid+"' ;");
                        rs1.first();
                        String temp1=rs1.getString(2);

                        rs2=st2.executeQuery("Select * from CostOfServices where ServiceID='"+sid+"' ;");
                        rs2.first();
                        String temp2=rs2.getString(2);
                        fbbill+="--------------------------------------------------\n";
                        fbbill+=temp1+"\t"+temp2+"\n";
                        fbbill+="--------------------------------------------------\n";
                        fcost=fcost+Integer.valueOf(temp2);


                    }
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
            if(doneby.equals("1"))
            {
                showmessage(ultimate);}
            else
            { fbbill+="Total Amount: "+Integer.toString(fcost);
                showmessage(fbbill);
               // Toast.makeText(getApplicationContext(),Integer.toString(count),Toast.LENGTH_LONG).show();
            }


        }
    }
}
