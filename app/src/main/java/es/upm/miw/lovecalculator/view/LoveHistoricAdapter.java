package es.upm.miw.lovecalculator.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import es.upm.miw.lovecalculator.databinding.ItemLoveHistoricBinding;
import es.upm.miw.lovecalculator.models.LoveCalculator;

public class LoveHistoricAdapter extends ArrayAdapter {

    private static LayoutInflater layoutInflater;
    private Context context;
    private int idLayout;
    private List<LoveCalculator> loveCalculators;

    public LoveHistoricAdapter(@NonNull Context context, int idLayout, @NonNull List<LoveCalculator> loveCalculators) {
        super(context, idLayout, loveCalculators);
        this.context = context;
        this.idLayout = idLayout;
        this.loveCalculators = loveCalculators;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (null == layoutInflater) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        ItemLoveHistoricBinding itemLoveHistoricBinding = DataBindingUtil.getBinding(convertView);
        if (itemLoveHistoricBinding == null) {
            itemLoveHistoricBinding = DataBindingUtil.inflate(layoutInflater, idLayout, parent, false);
        }
        itemLoveHistoricBinding.setLovecalculator(loveCalculators.get(position));
        itemLoveHistoricBinding.executePendingBindings();
        return itemLoveHistoricBinding.getRoot();
    }
}
