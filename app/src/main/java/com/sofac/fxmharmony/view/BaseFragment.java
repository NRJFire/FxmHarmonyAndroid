package com.sofac.fxmharmony.view;

import android.support.v4.app.Fragment;
import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.util.ProgressBar;

import static com.orm.SugarRecord.findById;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

   // public AppUserID appUserID;
    //public UserDTO userDTO;
    //public ProgressBar progressBar;

    public BaseFragment() {
        //progressBar = new ProgressBar(this.getContext());
        //appUserID = new AppUserID(this.getContext());
        //userDTO = findById(UserDTO.class, appUserID.getID());

    }
}