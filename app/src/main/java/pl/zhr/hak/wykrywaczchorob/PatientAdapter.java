package pl.zhr.hak.wykrywaczchorob;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.zhr.hak.wykrywaczchorob.activities.PatientPresentationActivity;

import static pl.zhr.hak.wykrywaczchorob.Disease.getDiseases;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    static class PatientViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final TextView textViewDisease;
        private final CheckBox checkBoxDelete;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.textViewDisease = itemView.findViewById(R.id.textViewDisease);
            this.checkBoxDelete = itemView.findViewById(R.id.checkBoxDelete);
        }
    }

    private List<Patient> mPatientList;
    private final LayoutInflater mInflater;
    private Context mContext;
    private List<Patient> patientsToDelete = new ArrayList<>();
    public PatientAdapter(List<Patient> patientList, Context context) {
        this.mPatientList = patientList;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.listitem_patient, parent, false);
        return new PatientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        List<Disease> diseaseList = getDiseases(mContext);
        holder.textViewName.setText(mContext.getString(R.string.patient,
                position + 1, mPatientList.get(position).getName(), mPatientList.get(position).getSurname()));
        holder.textViewName.setPaintFlags(holder.textViewName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.textViewName.setOnClickListener(v -> {
            Intent patientPresentationActivity = new Intent(mContext, PatientPresentationActivity.class);
            patientPresentationActivity.putExtra("patient", mPatientList.get(position));
            mContext.startActivity(patientPresentationActivity);
        });
        holder.textViewDisease.setText(diseaseList.get(mPatientList.get(position).getDiseaseID() - 1).getDiseaseName());
        // po usunięciu zaznacoznych pacjentów wszystkie checkboxy ustawione na false
        holder.checkBoxDelete.setChecked(false);
        holder.checkBoxDelete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                patientsToDelete.add(mPatientList.get(position));
            }
            else {
                patientsToDelete.remove(mPatientList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() { return mPatientList.size(); }

    public void setPatients(List<Patient> patients) {
        mPatientList = patients;
        notifyDataSetChanged();
    }

    public void deleteSelected(PatientViewModel patientViewModel) {
        for (Patient patient : patientsToDelete) {
            patientViewModel.delete(patient);
        }
    }
}

