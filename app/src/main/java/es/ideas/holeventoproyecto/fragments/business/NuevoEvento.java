package es.ideas.holeventoproyecto.fragments.business;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.Date;
import java.util.Calendar;

import es.ideas.holeventoproyecto.R;

public class NuevoEvento extends Fragment {


    public NuevoEvento() {}

    private View viewRoot;
    private EditText fechaEvento;
    private DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.nuevo_evento_fragment, container, false);
        // Inflate the layout for this fragment

        inicianVista();

        fechaEvento.setFocusable(false);
        fechaEvento.setHint(obtenerFechaActual());
        fechaEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(viewRoot);
            }
        });

        return viewRoot;
    }

    private void inicianVista(){
        initDatePicker();
        fechaEvento = (EditText) viewRoot.findViewById(R.id.etFechaEvento);
    }

    private String obtenerFechaActual(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return fechaToString(day, month, year);
    }

    private String fechaToString(int day, int month, int year){
        if (day<10 && month<10){
            return "0"+day+"/0"+month+"/"+year;
        }
        else if (day<10 && month>=10){
            return "0"+day+"/"+month+"/"+year;
        }
        else if (day>=10 && month<10){
            return day+"/0"+month+"/"+year;
        }
        else{
            return day+"/"+month+"/"+year;
        }
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = fechaToString(day, month, year);
                fechaEvento.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(viewRoot.getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private void openDatePicker(View view){
        datePickerDialog.show();
    }

}