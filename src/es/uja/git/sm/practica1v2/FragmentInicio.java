package es.uja.git.sm.practica1v2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentInicio extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View fragment= inflater.inflate(R.layout.fragment_main,container,false);
		return fragment;
		
	}

}
