package com.otpreader.BottomSheetIntilization;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.otpreader.R;
import com.otpreader.interfaces.PinePGBottomSheetBehaviour;
public class PinePGBottomSheetBehaviourDefault extends AppCompatActivity implements PinePGBottomSheetBehaviour {
    BottomSheetBehavior objBottomSheetBehaviour = null;
    private String TAG = "PinePGBottomSheetBehaviourDefault";
    private ImageButton btn = null;
    @Override
    public void setBottomSheetBehaviour(final View myView, View button) {
        try {
            if (myView == null || button == null) {
                return;
            }
            objBottomSheetBehaviour = BottomSheetBehavior.from(myView);
            btn = button.findViewById(R.id.dottedbutton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetMinimizationMethod(v);
                }
            });
            objBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            btn.setImageResource(R.drawable.arrowup);
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            btn.setImageResource(R.drawable.arrowdown);
                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:
                            objBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            //do nothing
                            break;
                    }
                }
                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "An Exception has occurred while setting bottom sheet behaviour", e);
        }
    }

    public void changeBottomSheetBehaviour(int stateId) {
        if (btn == null || objBottomSheetBehaviour == null)
            return;
        objBottomSheetBehaviour.setState(stateId);
    }

    public void bottomSheetMinimizationMethod(View v) {
        if (objBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            objBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            objBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}