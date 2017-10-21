package com.example.renan.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<String> linhaDoTempo;
    List<String> historico;
    List<String> linhaDoTempoDescription;
    List<String> historicoDescription;

    private RecyclerView recyclerViewLinhaDoTempo;
    private RecyclerView recyclerViewHistorico;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManagerLinhaDoTempo;
    RecyclerView.LayoutManager layoutManagerHistorico;
    LocationManager locationManager;
    public static final int MY_REQUEST_CODE = 100;
    TextView etLatitude;
    TextView etLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Indica que quer receber localizacao
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        //capturar rv do layout
        recyclerViewLinhaDoTempo = (RecyclerView)findViewById(R.id.rv_linha_do_tempo);
        recyclerViewHistorico = (RecyclerView) findViewById(R.id.rv_historico);

        //capturar EditText
        etLatitude = (TextView) findViewById(R.id.tv_latitude);
        etLongitude = (TextView)findViewById(R.id.tv_longitude);


        //otimizar recyle vie
        recyclerViewLinhaDoTempo.setHasFixedSize(true);
        recyclerViewHistorico.setHasFixedSize(true);

        //Cria um gerenciador de LinearLayout e o joga para dentro do gerenciador de layout
        layoutManagerLinhaDoTempo = new LinearLayoutManager(this);
        layoutManagerHistorico = new LinearLayoutManager(this);

        //Para cada um dos rv nos setamos o gerenciador criado acima
        recyclerViewLinhaDoTempo.setLayoutManager(layoutManagerLinhaDoTempo);
        recyclerViewHistorico.setLayoutManager(layoutManagerHistorico);

        //Criando o arraylist dos nomes e do historico
        linhaDoTempo = new ArrayList<String>();

        historico = new ArrayList<String>();

        linhaDoTempoDescription = new ArrayList<String>();

        historicoDescription = new ArrayList<String>();

        //Criando array de Strings para serem inseridos nas listas
        List<String> nomes = criarArrayDeNomes();
        List<String> description = criarArrayDeDescription();
        List<String> notifications = criarArrayDeNotificacoes();
        List<String> notificationDescriptions = criarArrayDeDescricaoDeNotificacoes();

        for (int i = 0;i<10;i++){

            Random random = new Random();
            int numberOfRandom = random.nextInt(3);
            int numberOfRandomNotification = random.nextInt(4);

            linhaDoTempo.add(nomes.get(numberOfRandom));
            linhaDoTempoDescription.add(description.get(numberOfRandom));

            historico.add(notifications.get(numberOfRandomNotification));
            historicoDescription.add(notificationDescriptions.get(numberOfRandomNotification));

        }





       /* //especificando adapter
        adapter = new MyAdapter(linhaDoTempo,linhaDoTempoDescription,1);
        recyclerViewLinhaDoTempo.setAdapter(adapter);

        adapter = new MyAdapter(historico,historicoDescription,2);
        recyclerViewHistorico.setAdapter(adapter);*/





    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Teste de gravacao Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("MeusDados");
        DatabaseReference dbr = databaseReference.child("nome");
        dbr.setValue("Renan Lima");

        if(checarPermissaoDeLocalizacao()){

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, locationListener);
            }
        }
    }

    private boolean checarPermissaoDeLocalizacao() {


        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){

                new AlertDialog.Builder(this)
                        .setTitle("Permissao")
                        .setMessage("Solicitacao de localizacao")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();


            }else{

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_REQUEST_CODE);
            }

            return false;
        }else{
            return true;
        }


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_REQUEST_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
                    }
                }

        }
    }

    private List<String> criarArrayDeDescription() {
        List<String> descriptions = new ArrayList<String>();
        descriptions.add("A pessoa tal passou por voce");
        descriptions.add("A pessoa tal esteve nom mesmo evento que voce");
        descriptions.add("A pessoa tal aceitou se conectar a voce");
    return descriptions;}

    private List<String> criarArrayDeNomes() {

        List<String> nomes = new ArrayList<String>();
        nomes.add("Fulano");
        nomes.add("Beltrano");
        nomes.add("Ciclano");

        return nomes;}

    private List<String> criarArrayDeNotificacoes(){

        List<String> notificacoesTitle = new ArrayList<String>();
        notificacoesTitle.add("Mensagem");
        notificacoesTitle.add("Evento");
        notificacoesTitle.add("Sugestoes");
        notificacoesTitle.add("Propostas");

    return notificacoesTitle;}

    private List<String> criarArrayDeDescricaoDeNotificacoes() {
        List<String> notificacoesDescription = new ArrayList<String>();
        notificacoesDescription.add("Ha uma nova mensagem para voce");
        notificacoesDescription.add("Um evento esta ocorrendo");
        notificacoesDescription.add("Veja sugestoes de amizade");
        notificacoesDescription.add("Uma proposta esta aguardando sua avaliacao");

        return notificacoesDescription;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            etLongitude.setText(String.valueOf(location.getLongitude()));
            etLatitude.setText(String.valueOf(location.getLatitude()));
            //Sending Geolocation to Firebase
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("MeusDados");
            DatabaseReference dbrLatitude = databaseReference.child("latitude");
            DatabaseReference dbrLongitude = databaseReference.child("longitude");
            dbrLatitude.setValue(location.getLatitude());
            dbrLongitude.setValue(location.getLongitude());

            //



        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    public void realizarCheckIn(View view) {
        Toast.makeText(getApplicationContext(), "Voce fez o checkin em um evento", Toast.LENGTH_SHORT).show();
        //especificando adapter
        adapter = new MyAdapter(linhaDoTempo,linhaDoTempoDescription,1);
        recyclerViewLinhaDoTempo.setAdapter(adapter);

        adapter = new MyAdapter(historico,historicoDescription,2);
        recyclerViewHistorico.setAdapter(adapter);
    }
}


