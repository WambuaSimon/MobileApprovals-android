package com.wizag.mobileapprovals.viewModel;

import android.arch.lifecycle.ViewModel;

public class AdminDocumentsViewModel extends ViewModel {

    int counter = 0;

    public int getInitialCount(){
        return counter;
    }
    public int getCurrentCount(){
        counter += 1;
       return counter;
    }

}
