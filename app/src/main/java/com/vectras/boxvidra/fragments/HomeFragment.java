package com.vectras.boxvidra.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Build;
import android.system.ErrnoException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.termux.app.TermuxActivity;
import com.vectras.boxvidra.R;
import com.vectras.boxvidra.core.TermuxX11;
import com.vectras.boxvidra.services.MainService;

import static android.os.Build.VERSION.SDK_INT;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public static MaterialButton startX11Btn, startXfce4Btn, openTerminalBtn;
    public static TextView termuxX11TextView, xfce4TextView, tvLogger;

    private boolean isX11Started = false;
    private boolean isXFCE4Started = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        startX11Btn = view.findViewById(R.id.startX11);
        startX11Btn.setOnClickListener(this);

        startXfce4Btn = view.findViewById(R.id.startXfce4);
        startXfce4Btn.setOnClickListener(this);

        openTerminalBtn = view.findViewById(R.id.openTerminal);
        openTerminalBtn.setOnClickListener(this);

        termuxX11TextView = view.findViewById(R.id.tvIsTermuxX11);
        xfce4TextView = view.findViewById(R.id.tvIsXfce4);
        tvLogger = view.findViewById(R.id.tvLogger);
        tvLogger.setTypeface(Typeface.MONOSPACE);

        if (isX11Started) {
            termuxX11TextView.setText(R.string.termuxx11_service_yes);
        } else {
            termuxX11TextView.setText(R.string.termuxx11_service_no);
        }

        if (isXFCE4Started) {
            xfce4TextView.setText(R.string.xfce4_service_yes);
        } else {
            xfce4TextView.setText(R.string.xfce4_service_no);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.startX11) {
            try {
                isX11Started = true;
                startX11Btn.setEnabled(false);
                termuxX11TextView.setText(R.string.termuxx11_service_yes);
                TermuxX11.main(new String[]{":0"});
            } catch (ErrnoException e) {
                isX11Started = false;
                startX11Btn.setEnabled(true);
                termuxX11TextView.setText(R.string.termuxx11_service_no);
            }
        } else if (id == R.id.startXfce4) {
            Intent serviceIntent = new Intent(requireActivity(), MainService.class);
            if (SDK_INT >= Build.VERSION_CODES.O) {
                requireActivity().startForegroundService(serviceIntent);
            } else {
                requireActivity().startService(serviceIntent);
            }
            isXFCE4Started = true;
            startXfce4Btn.setEnabled(false);
            xfce4TextView.setText(R.string.xfce4_service_yes);
        } else if (id == R.id.openTerminal) {
            startActivity(new Intent(requireActivity(), TermuxActivity.class));
        }
    }
}
