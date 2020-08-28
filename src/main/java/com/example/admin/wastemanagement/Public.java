package com.example.admin.wastemanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Public extends AppCompatActivity implements View.OnClickListener {
    ImageButton ib1, ib2, ib3, ib4;
    int count;
    Connection con;
    Statement st;
    private String url = "jdbc:mysql://waste-management-database.cqkj1kgoaaet.us-east-1.rds.amazonaws.com:3306/wastemanagement";
    private String usr = "root";
    private String pwd = "password";
    String pid = "";
    String[] s = new String[5];
    String place ,doorno ,phone,name,action,info="",fbbill="";
    int fcost=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        Bundle b = getIntent().getExtras();
        pid = b.getString("id");
        ib1 = findViewById(R.id.info);
        ib2 = findViewById(R.id.pubinfo);
        ib3 = findViewById(R.id.publicservices);
        ib4 = findViewById(R.id.bill);
        ib1.setImageResource(R.drawable.info);
        ib2.setImageResource(R.drawable.pubdetails);
        ib3.setImageResource(R.drawable.publicservices);
        ib4.setImageResource(R.drawable.bill);
        ib1.setOnClickListener(this);
        ib2.setOnClickListener(this);
        ib3.setOnClickListener(this);
        ib4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==ib1.getId())
        {
            action="Info";
            new myTask().execute();
        }
        if(view.getId()==ib2.getId())
        {
            action="PDetails";
            new myTask().execute();

        }
        if (view.getId() == ib3.getId()) {

            Intent i =new Intent(this,PublicServices.class);
            startActivity(i);
        }
        if (view.getId()==ib4.getId())
        {
            action="bill";
            new myTask().execute();
        }


    }

    public void showmessage(String s)
    {
        AlertDialog.Builder al =
                new AlertDialog.Builder(
                        Public.this);
        if(action.equals("PDetails"))
        {al.setTitle("Personal Details");
        al.setIcon(R.drawable.details);}
        else if(action.equals("Info"))
        {al.setTitle("Service Provided");
            al.setIcon(R.drawable.info);
        }
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

    protected void onPreExecute()
    {
        super.onPreExecute();
        prgDialog = new ProgressDialog(
                Public.this);
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
            ResultSet rs1,rs2;
            if(action.equals("PDetails")) {
                ResultSet rs = st.executeQuery("Select * from Public where PublicID = '" + pid + "' ;");
                rs.first();
                name = rs.getString(3);
                rs = st.executeQuery("Select * from PublicDetails where Name= '" + name + "' ;");
                rs.first();

                place = rs.getString(2);
                doorno = rs.getString(3);
                phone = rs.getString(4);
            }
            else if(action.equals("Info"))
            {
                ResultSet rs = st.executeQuery("Select * from CompanyServices ;");
                while(rs.next())
                {
                    String temp1,temp2;
                    temp1=rs.getString(1);
                    temp2=rs.getString(2);
                    info+=temp1+"\t"+temp2+"\n";

                }


            }
            else
            {

                ResultSet rs = st.executeQuery("Select * from ServicesRequestedByPublic where PublicID='"+pid+"' ;");
                 count=0;
                while(rs.next())
                {//count++;
                    String sid=rs.getString(2);
                    rs1=st1.executeQuery("Select * from PublicServices where ServiceID='"+sid+"' ;");
                    rs1.first();
                    String temp1=rs1.getString(2);

                     rs2=st2.executeQuery("Select * from CostOfServices where ServiceID='"+sid+"' ;");
                    rs2.first();
                    String temp2=rs2.getString(2);
                    fbbill+=temp1+"\t"+temp2+"\n";

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
        if(action.equals("Info"))
        {Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            showmessage(info);}
        else if(action.equals("PDetails"))
        {
            showmessage("Name:"+name+"\n"+"Place:"+place+"\n"+"Doorno:"+doorno+"\nPhone:"+phone);}

        else
            { fbbill+="Total Amount: "+Integer.toString(fcost);
            showmessage(fbbill);
                Toast.makeText(getApplicationContext(),Integer.toString(count),Toast.LENGTH_LONG).show();
            }



}
}}