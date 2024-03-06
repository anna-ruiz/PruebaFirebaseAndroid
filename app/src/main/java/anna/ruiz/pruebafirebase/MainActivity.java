package anna.ruiz.pruebafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText txtFrase;
    private TextView lbFrase;
    private Button btnSave;
    private FirebaseDatabase database;
    private DatabaseReference refFrase;
    private DatabaseReference refPersona; //referencia al objeto persona!
    private ArrayList<Persona> personas;
    private DatabaseReference refPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtFrase = findViewById(R.id.txtFrase);
        lbFrase = findViewById(R.id.lbFrase);
        btnSave = findViewById(R.id.btnSave);

        //pasamos la url de la BD para la conexion (evitamos errores si hay varias)
        database = FirebaseDatabase.getInstance("https://pruebafirebase-3864f-default-rtdb.europe-west1.firebasedatabase.app/");

        //lista personas
        personas = new ArrayList<>();
        crearPersonas();
        refPersonas = database.getReference("personas");

        refFrase = database.getReference("frase");
        //A la referencia debemos agregar el valueEventlistener para q este pendiente de los cambios en la bd para leerlos
        refFrase.addValueEventListener(new ValueEventListener() {
            @Override //si hay un cambio se activa este metodo automaticamente
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) { //comprobamos q la ref existe
                //debemos hacer un cast al dato q trae de la bd con el tipo q esperamos
                    String frase = snapshot.getValue(String.class);
                    //Se asigna al label para q se muestre
                    lbFrase.setText(frase);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guarda en la bd en la referencia el valor del txt de la app
                refFrase.setValue(txtFrase.getText().toString());

                //persona
                int random = (int) (Math.random()*100);
                Persona p = new Persona(txtFrase.getText().toString(), random);
                refPersona.setValue(p);

                //lista personas
                refPersonas.setValue(personas);
            }
        });


        //persona
        refPersona = database.getReference("persona");

        refPersona.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Persona p = snapshot.getValue(Persona.class);
                Toast.makeText(MainActivity.this, p.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //lista personas
        refPersonas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //al ser listas necesitamos el genericTypeIndicator para q lo transforme
                        //En la ventana emergente de constructor no selecionamos ningun metodo!! la cerramos
                    GenericTypeIndicator<ArrayList<Persona>> gti = new GenericTypeIndicator<ArrayList<Persona>>() {
                    };
                    ArrayList<Persona> aux = snapshot.getValue(gti);
                    Toast.makeText(MainActivity.this, "Descargados: "+personas.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void crearPersonas(){
        for (int i = 0; i < 100; i++) {
            personas.add(new Persona("Persona "+i, (int) (Math.random()*100)));
        }
    }
}