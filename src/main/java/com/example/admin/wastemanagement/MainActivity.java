package com.example.admin.wastemanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog prgDialog;
    Button b1;
    EditText e1,e2;
    TextView ttt,t;
    String s1,s2,decision="-1";
    String doneby="-1";
    Connection con;
    Statement st;

    //String s="";
    private String url="jdbc:mysql://waste-management-database.cqkj1kgoaaet.us-east-1.rds.amazonaws.com:3306/wastemanagement";
    private String usr="root";
    private String pwd="password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        b1=findViewById(R.id.b1);
        b1.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        s1=e1.getText().toString();
        s2=e2.getText().toString();

        if(s2.charAt(0)=='P')
        {
            doneby="P";
        }
        else if(s2.charAt(0)=='C')
        {
            doneby="C";
        }
        else if(s2.charAt(0)=='W')
        {
            doneby="W";
        }
        else if(s2.charAt(0)=='A')
        {
            doneby="A";
        }

        new myTask().execute();

    }
    private class myTask extends AsyncTask<Void,Void,Void>
    { String s="";

        protected void onPreExecute()
        {
            super.onPreExecute();
            prgDialog = new ProgressDialog(
                    MainActivity.this);
            prgDialog.setMessage
                    ("Verifying Details");
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
                     con= DriverManager.getConnection(url,usr,pwd);
                     st=con.createStatement();



                if(doneby.equals("P"))
                {

                    final ResultSet rs=st.executeQuery("SELECT * FROM Public WHERE Name ="+"'"+s1+"'"+" AND PublicID = "+"'"+s2+"' ;");
                    if(rs.first())
                    {

                        decision="1";
                    }
                    else
                    {
                        decision="0";
                    }
                    con.close();
                }
                else if(doneby.equals("A"))
                {




                    final ResultSet rs=st.executeQuery("SELECT * FROM Admin WHERE Name ="+"'"+s1+"'"+" AND AdminID = "+"'"+s2+"' ;");

                    if(rs.last()) {
                        s = rs.getString(1)+" "+rs.getString(2);



                    }

                    if(s.length()!=0)
                    {
                        decision="1";


                    }
                    else {

                        decision="0";


                    }
                    con.close();
                }
                else if(doneby.equals("W")) {

                     final ResultSet rs=st.executeQuery("SELECT * FROM Worker WHERE Name ="+"'"+s1+"'"+" AND WorkerID = "+"'"+s2+"' ;");
                    if (rs.next()) {
                        s = rs.getString(1)+""+rs.getString(2);
                    }
                     if(s.length()!=0)
                     {
                         decision="1";


                     }
                     else {

                         decision="0";


                     }
                     con.close();

                }
                else if(doneby.equals("C")) {
                     final ResultSet rs=st.executeQuery("SELECT * FROM Companies WHERE Name ="+"'"+s1+"'"+" AND CompanyID = "+"'"+s2+"' ;");
                     if (rs.next()) {
                         s = rs.getString(1)+" "+rs.getString(2);
                     }
                     if(s.length()!=0)
                     {
                         decision="1";


                     }
                     else {

                         decision="0";


                     }
                     con.close();
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
            if(decision.equals("0")||decision.equals("-1"))
            Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
           else
            {
                if(doneby.equals("P"))
                {
                    Intent i=new Intent(getApplicationContext(),Public.class);
                    i.putExtra("id",s2);
                    startActivity(i);
                }
                else if(doneby.equals("A"))
                {
                    Intent i=new Intent(getApplicationContext(),Admin.class);
                    i.putExtra("id",s2);
                    startActivity(i);
                }
                else if(doneby.equals("C"))
                {
                    Intent i=new Intent(getApplicationContext(),Company.class);
                    i.putExtra("id1",s2);
                    startActivity(i);
                }
                else if(doneby.equals("W"))
                {
                    Intent i=new Intent(getApplicationContext(),Worker.class);
                    i.putExtra("id2",s2);
                    startActivity(i);
                }
            }

        }
    }
}
