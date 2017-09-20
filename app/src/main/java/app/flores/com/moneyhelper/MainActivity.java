package app.flores.com.moneyhelper;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static int INGRESO = 0;
    private TextView saldoActualAhorro;
    private TextView saldoActualEfectivo;
    private TextView saldoActualTarjeta;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private PieChart pieChart;

    Operacion op = new Operacion();
    double montoIngreso = op.getMontoIngreso();
    double montoEgreso = op.getMontoEgreso();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Cambiar el color del button:
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        int colorPrimary = getResources().getColor(R.color.colorAccentlight);
        ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{colorPrimary});
        floatingActionButton.setBackgroundTintList(csl);

        pieChart = (PieChart) findViewById(R.id.pieChart);

        pieChart.setDescription(null);
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Registro de Ahorros");
        pieChart.setCenterTextSize(10);

        mostrarPye();

        saldoActualAhorro = (TextView) findViewById(R.id.saldoActualAhorro);
        saldoActualEfectivo = (TextView) findViewById(R.id.saldoActualEfectivo);
        saldoActualTarjeta = (TextView) findViewById(R.id.saldoActualCredito);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        double montoCredito = op.getMontoCredito();
        double montoAhorro = op.getMontoAhorro();
        double montoEfectivo = op.getMontoEfectivo();
        saldoActualTarjeta.setText("$/."+montoCredito);
        saldoActualEfectivo.setText("$/."+montoEfectivo);
        saldoActualAhorro.setText("$/."+montoAhorro);

        double porcentaje = montoIngreso*100/(montoIngreso+montoEgreso);
        int x = (int) porcentaje;
        progressBar.setProgress(x);
    }

    public void mostrarPye(){
        double montoIngreso = op.getMontoIngreso();
        double montoEgreso = op.getMontoEgreso();
        float value1 = (float)montoIngreso;
        float value2 = (float)montoEgreso;

        float [] datas = {value1, value2};
        ArrayList<PieEntry> pEntry = new ArrayList<>();

        for(int i=0;i< datas.length; i++){
            pEntry.add(new PieEntry(datas[i],i));
        }

        PieDataSet pieDataSet = new PieDataSet(pEntry, "Estado de Cuentas");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    public void agregarTransaccion(View view){
        Intent i = new Intent(MainActivity.this, FormActivity.class);
        // Iniciamos la segunda actividad, y le indicamos que la iniciamos
        startActivityForResult(i, INGRESO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
        if (resultCode == RESULT_CANCELED) {
            // Si es así mostramos mensaje de cancelado por pantalla.
            Toast.makeText(this, "Proceso cancelado", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // De lo contrario, recogemos el resultado de la segunda actividad.
            double resultado = data.getExtras().getDouble("RESULTADO");
            String resultado2 = data.getExtras().getString("RESULTADO2");
            String resultado3 = data.getExtras().getString("RESULTADO3");
            // Y tratamos el resultado en función de si se lanzó para rellenar
            switch (requestCode) {
                case INGRESO:

                    if("Tarjeta de Credito".equals(resultado3)){

                        if("Ingreso".equals(resultado2)){
                            double montoCredito = op.getMontoCredito();//Resultado de la clase
                            double montoIngreso = op.getMontoIngreso();
                            montoCredito += resultado;
                            montoIngreso += resultado;
                            op.setMontoCredito(montoCredito);
                            op.setMontoIngreso(montoIngreso);

                            Toast.makeText(MainActivity.this, "Se registro correctamente su ingreso!", Toast.LENGTH_SHORT).show();
                            saldoActualTarjeta.setText("$/."+montoCredito);
                        }else{
                            //Si el proceso es un egreso
                            double montoCredito = op.getMontoCredito();//Resultado de la clase
                            double montoEgreso = op.getMontoEgreso();

                            if(montoCredito<resultado){
                                Toast.makeText(MainActivity.this, "No se pudo realizar la transaccion, ingrese un monto valido!", Toast.LENGTH_SHORT).show();
                            }else{
                                montoCredito -= resultado;
                                montoEgreso += resultado;
                                op.setMontoCredito(montoCredito);
                                op.setMontoEgreso(montoEgreso);
                                saldoActualTarjeta.setText("$/."+montoCredito);
                                Toast.makeText(MainActivity.this, "Se registro correctamente su egreso!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else if("Ahorro".equals(resultado3)){
                        if("Ingreso".equals(resultado2)){
                            double montoAhorro = op.getMontoAhorro();//Resultado de la clase
                            double montoIngreso = op.getMontoIngreso();
                            montoAhorro += resultado;
                            montoIngreso += resultado;
                            op.setMontoAhorro(montoAhorro);
                            op.setMontoIngreso(montoIngreso);

                            Toast.makeText(MainActivity.this, "Se registro correctamente su ingreso!", Toast.LENGTH_SHORT).show();
                            saldoActualAhorro.setText("$/."+montoAhorro);
                        }else{
                            //Si el proceso es un egreso
                            double montoAhorro = op.getMontoAhorro();//Resultado de la clase
                            double montoEgreso = op.getMontoEgreso();

                            if(montoAhorro<resultado){
                                Toast.makeText(MainActivity.this, "No se pudo realizar la transaccion, ingrese un monto valido!", Toast.LENGTH_SHORT).show();
                            }else{
                                montoAhorro -= resultado;
                                montoEgreso += resultado;
                                op.setMontoAhorro(montoAhorro);
                                op.setMontoEgreso(montoEgreso);
                                saldoActualAhorro.setText("$/."+montoAhorro);
                                Toast.makeText(MainActivity.this, "Se registro correctamente su egreso!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        if("Ingreso".equals(resultado2)){
                            double montoEfectivo = op.getMontoEfectivo();//Resultado de la clase
                            double montoIngreso = op.getMontoIngreso();
                            montoEfectivo += resultado;
                            montoIngreso += resultado;
                            op.setMontoEfectivo(montoEfectivo);
                            op.setMontoIngreso(montoIngreso);

                            Toast.makeText(MainActivity.this, "Se registro correctamente su ingreso!", Toast.LENGTH_SHORT).show();
                            saldoActualEfectivo.setText("$/."+montoEfectivo);
                        }else{
                            //Si el proceso es un egreso
                            double montoEfectivo = op.getMontoEfectivo();//Resultado de la clase
                            double montoEgreso = op.getMontoEgreso();

                            if(montoEfectivo<resultado){
                                Toast.makeText(MainActivity.this, "No se pudo realizar la transaccion, ingrese un monto valido!", Toast.LENGTH_SHORT).show();
                            }else{
                                montoEfectivo -= resultado;
                                montoEgreso += resultado;
                                op.setMontoEfectivo(montoEfectivo);
                                op.setMontoEgreso(montoEgreso);
                                saldoActualEfectivo.setText("$/."+montoEfectivo);
                                Toast.makeText(MainActivity.this, "Se registro correctamente su egreso!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    //Se usa para completar el progressbar
                    double montoIngreso2 = op.getMontoIngreso();
                    double montoEgreso2 = op.getMontoEgreso();

                    double porcentaje2 = montoIngreso2*100/(montoIngreso2+montoEgreso2);
                    int x = (int) porcentaje2;
                    progressBar.setProgress(x);
                    mostrarPye();
                    break;
            }
        }
    }
}