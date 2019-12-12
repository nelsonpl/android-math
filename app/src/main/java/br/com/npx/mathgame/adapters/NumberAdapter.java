package br.com.npx.mathgame.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.npx.mathgame.R;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {

    private NumberCustom[] mData;
    private LayoutInflater mInflater;
    private Boolean isResult;
    private ItemClickListener mClickListener;

    public NumberAdapter(Context context, Integer[] data, Boolean isResult) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = new NumberCustom[data.length];
        this.isResult = isResult;

        for (int i = 0; i < data.length; i++) {
            mData[i] = new NumberCustom();
            mData[i].number = (data[i]);
            mData[i].status = true;
            mData[i].alternative = false;
        }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (isResult)
            view = mInflater.inflate(R.layout.result_item, parent, false);
        else
            view = mInflater.inflate(R.layout.option_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NumberCustom number = mData[position];
        Resources resources = mInflater.getContext().getResources();
        if (isResult) {
            holder.textView.setText(String.valueOf(number.number));
            holder.textView.setBackgroundColor(resources.getColor(mData[position].status ? R.color.result_active : R.color.result_inactive));
        } else {
            holder.button.setText(String.valueOf(number.number));
            holder.button.setBackgroundColor(resources.getColor(mData[position].alternative ? R.color.option_alternative : mData[position].status ? R.color.option_active : R.color.option_inactive));
        }
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public int findResult(Integer sum) {
        for (int i = 0; i < mData.length; i++) {
            NumberCustom result = mData[i];
            if (!result.status) {
                continue;
            }

            if (result.number.equals(sum)) {
                result.status = (false);
                return i;
            }
        }
        return -1;
    }

    public void setStatus(int position, boolean status) {
        mData[position].status = (status);
    }

    public int setAlternative(int number) {
        for (int i = 0; i < mData.length; i++) {
            NumberCustom n = mData[i];
            if (n.number == number) {
                n.alternative = (true);
                return i;
            }
        }
        return -1;
    }

    public void setAlternative(int position, boolean alternative) {
        mData[position].alternative = alternative;
    }

    public boolean allStatus() {

        for (NumberCustom aMData : mData) {
            if (aMData.status) {
                return false;
            }
        }

        return true;
    }

    public void setData(Integer[] data) {
        for (int i = 0; i < data.length; i++) {
            mData[i] = new NumberCustom();
            mData[i].number = (data[i]);
            mData[i].status = true;
            mData[i].alternative = false;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        Button button;

        ViewHolder(View itemView) {
            super(itemView);
            if (isResult)
                textView = itemView.findViewById(R.id.info_text);
            else {
                button = itemView.findViewById(R.id.btOption);
                button.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Integer getNumber(int position) {
        return mData[position].number;
    }

    public Boolean getStatus(int position) {
        return mData[position].status;
    }

    public void activeItem(int position) {
        mData[position].status = (true);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    private class NumberCustom {
        private Integer number;
        private Boolean status;
        private boolean alternative;

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

