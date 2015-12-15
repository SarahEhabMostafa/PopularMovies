package com.sarahehabm.popularmovies.view;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.sarahehabm.popularmovies.controller.OnRetrieveListener;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public class BaseFragment extends Fragment implements OnRetrieveListener {
    protected ProgressDialog progressDialog;

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading..", true, false);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
