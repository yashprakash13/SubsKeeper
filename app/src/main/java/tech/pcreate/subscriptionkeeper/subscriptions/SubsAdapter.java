package tech.pcreate.subscriptionkeeper.subscriptions;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tech.pcreate.subscriptionkeeper.R;
import tech.pcreate.subscriptionkeeper.database.Subscription;
import tech.pcreate.subscriptionkeeper.utils.ActivityUtils;
import tech.pcreate.subscriptionkeeper.utils.DateFormatter;

public class SubsAdapter extends RecyclerView.Adapter<SubsAdapter.SubViewHolder> {

    private List<Subscription> subscriptionList =  new ArrayList<>();
    private SubsContract.Presenter.OnItemClickListener onItemClickListener;

    private int[] mMaterialColors;
    private static final Random RANDOM = new Random();
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;


    public SubsAdapter(SubsContract.Presenter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_list,
                    parent,
                    false);
        mMaterialColors = parent.getContext().getResources().getIntArray(R.array.rainbow);
        mBackground = mTypedValue.resourceId;
        view.setBackgroundResource(mBackground);
        return new SubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubViewHolder holder, int position) {

        holder.subscription = subscriptionList.get(position);
        final Subscription subscription = subscriptionList.get(position);
        holder.nameOfSub.setText(subscription.getName());
        holder.amount.setText(String.format("%s %s", subscription.getCurrency(), String.valueOf(subscription.getAmount())));

        holder.materialLetterIcon.setInitials(true);
        holder.materialLetterIcon.setInitialsNumber(1);
        holder.materialLetterIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        holder.materialLetterIcon.setLetter(subscription.getName());
        holder.materialLetterIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);

        holder.nextDueOn.setText(DateFormatter.format(subscription.getBeginDate()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.clickItem(holder.subscription);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemClickListener.clickLongItem(holder.subscription);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    public void setSubscriptionsList(List<Subscription> list){
        subscriptionList = list;
        notifyDataSetChanged();
    }

    public class SubViewHolder extends RecyclerView.ViewHolder{

        public View mView;
        public TextView nameOfSub, nextDueOn, amount;
        public MaterialLetterIcon materialLetterIcon;

        public Subscription subscription;

        public SubViewHolder(@NonNull View mView) {
            super(mView);
            this.mView = mView;

            nameOfSub = mView.findViewById(R.id.item_name);
            nextDueOn = mView.findViewById(R.id.next_due_on);
            amount =  mView.findViewById(R.id.amount);
            materialLetterIcon = mView.findViewById(R.id.letterIcon);
        }
    }
}