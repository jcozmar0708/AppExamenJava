package es.iesoretania.appexamen.Actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.iesoretania.appexamen.Miscelanea.AdminSQLiteOpenHelper;
import es.iesoretania.appexamen.R;
import es.iesoretania.appexamen.databinding.ActivityRegistrarBinding;

public class RegistrarActivity extends AppCompatActivity {

    private ActivityRegistrarBinding binding;
    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityRegistrarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items_spinner, android.R.layout.simple_spinner_item);
        binding.spinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSharedPreferences", Context.MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario","");
        String contrasenia = sharedPreferences.getString("contrasenia","");

        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);

        binding.botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminSQLiteOpenHelper.usuarioExiste(usuario)) {
                    mostrarAlerta("El usuario ya existe");
                } else {
                    String nombre = String.valueOf(binding.EditTextNombre.getText());
                    String apellidos = String.valueOf(binding.EditTextApellidos.getText());
                    String sexo = String.valueOf(binding.spinner.getSelectedItem());

                    AdminSQLiteOpenHelper.Usuario usuarioObject = new AdminSQLiteOpenHelper.Usuario(usuario, contrasenia, nombre, apellidos, sexo);

                    long valor = adminSQLiteOpenHelper.insertarUsuario(usuarioObject);

                    if (valor == -1) {
                        Toast.makeText(view.getContext(), "Error al insertar usuario", Toast.LENGTH_LONG).show();
                    }
                    else if (valor > 0) {
                        Toast.makeText(view.getContext(), "Usuario insertado correctamente", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(view.getContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void mostrarAlerta(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle("Error")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }
}