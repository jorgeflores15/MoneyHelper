package app.flores.com.moneyhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
public class FormActivity extends AppCompatActivity {

    private static final String[] SPINNERLIST = {"Tarjeta de Credito", "Ahorro", "Efectivo"};
    private MaterialBetterSpinner materialDesignSpinner;

        private EditText inputMonto;
        private RadioButton ingreso;
        private RadioButton egreso;
        private MaterialBetterSpinner tipoCuenta;
        private Button btnAceptar;

        String tipoTransaccion="";
        String tipoDeCuenta;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //Declaracion especial para usar Design en el Spinner:
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);

        // Enlazamos las variables con los componentes que tenemos en el XML
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        inputMonto = (EditText) findViewById(R.id.inputMonto);
        tipoCuenta = (MaterialBetterSpinner) findViewById(R.id.spinner);
        ingreso = (RadioButton) findViewById(R.id.ingreso);
        egreso = (RadioButton) findViewById(R.id.egreso);
        ingreso.setChecked(true);

        // Definimos el listener que ejecutará el método onClick del botón aceptar.
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si el EditText no está vacío recogemos el resultado.
                if (inputMonto.getText().length() != 0) {

                    if (ingreso.isChecked()) {
                        tipoTransaccion = "Ingreso";
                    } else if (egreso.isChecked()) {
                        tipoTransaccion = "Egreso";
                    } else {
                        Toast.makeText(FormActivity.this, "Seleccione un tipo de transaccion", Toast.LENGTH_SHORT).show();
                    }

                    tipoDeCuenta = tipoCuenta.getText().toString();

                    if (!tipoDeCuenta.equals("")) {

                        // Guardamos el texto del EditText en un Double.
                        double resultado = Double.parseDouble(inputMonto.getText().toString());
                        // Recogemos el intent que ha llamado a esta actividad.
                        Intent i = getIntent();
                        // Le metemos el resultado que queremos mandar a la actividad principal.
                        i.putExtra("RESULTADO", resultado);
                        i.putExtra("RESULTADO2", tipoTransaccion);
                        i.putExtra("RESULTADO3", tipoDeCuenta);
                        // Establecemos el resultado, y volvemos a la actividad principal. La variable que introducimos en primer lugar
                        // "RESULT_OK" es de la propia actividad, no tenemos que declararla nosotros.
                        setResult(RESULT_OK, i);

                        // Finalizamos la Activity para volver a la anterior
                        finish();

                    } else {
                        tipoCuenta.setError("Seleccione un tipo de cuenta");
                    }
                } else {
                    // Si no tenía nada escrito el EditText lo avisamos.
                    inputMonto.setError("Por favor ingrese un monto!");
                }
            }
        });
    }
}
