package tech.pcreate.subscriptionkeeper.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import tech.pcreate.subscriptionkeeper.R;
import tech.pcreate.subscriptionkeeper.subscriptions.SubsContract;

public class DeleteConfirmFragment extends DialogFragment {
    private SubsContract.Presenter.DeleteListener deleteListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final long subId = getArguments().getLong(AppConstants.SUB_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.are_you_sure);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteListener.setConfirm(true, subId);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteListener.setConfirm(false, subId);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        deleteListener = (SubsContract.Presenter.DeleteListener) context;
    }
}
