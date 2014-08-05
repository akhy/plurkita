package net.akhyar.plurkita;

import com.dd.processbutton.iml.ActionProcessButton;

/**
 * @author akhyar
 */
public class ButtonUtil {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_PROCESS = 1;
    public static final int STATE_COMPLETE = 2;
    public static final int STATE_ERROR = 3;

    public static void setState(ActionProcessButton button, int state, boolean endless) {
        if (endless)
            button.setMode(ActionProcessButton.Mode.ENDLESS);
        else
            button.setMode(ActionProcessButton.Mode.PROGRESS);

        switch (state) {
            case STATE_NORMAL:
                button.setProgress(0);
                break;

            case STATE_PROCESS:
                button.setProgress(50);
                break;

            case STATE_COMPLETE:
                button.setProgress(100);
                break;

            case STATE_ERROR:
                button.setProgress(-1);
                break;
        }
        button.setEnabled(state != STATE_PROCESS);
    }
}
