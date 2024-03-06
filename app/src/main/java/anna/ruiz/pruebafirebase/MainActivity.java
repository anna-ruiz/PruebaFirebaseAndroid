package anna.ruiz.pruebafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText txtFrase;
    private TextView lbFrase;
    private Button btnSave;
    private FirebaseDatabase database;
    private DatabaseReference refFrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtFrase = findViewById(R.id.txtFrase);
        lbFrase = findViewById(R.id.lbFrase);
        btnSave = findViewById(R.id.btnSave);

        //pasamos la url de la BD para la conexion (evitamos errores si hay varias)
        database = FirebaseDatabase.getInstance("https://pruebafirebase-3864f-default-rtdb.europe-west1.firebasedatabase.app/");
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
            }
        });
    }
}