package util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class InputValidator {

    public static boolean validateLoginInput(String username, String password, boolean showAlerts) {
        int usernameLength = username.length();
        if(!(usernameLength >= 3 && usernameLength <= 20)) {
            if(showAlerts) {
                if(usernameLength < 3) {
                    ScreenLoader.getInstance().displayAlert("Username Too Short", "The given username has less than 3 characters", Alert.AlertType.ERROR);
                } else if(usernameLength > 20) {
                    ScreenLoader.getInstance().displayAlert("Username Too Long", "The given username exceeded the maximum length of 20 characters", Alert.AlertType.ERROR);
                }
            }

            return false;
        }

        int passwordLength = password.length();
        if(!(passwordLength >= 6 && passwordLength <= 20)) {
            if(showAlerts) {
                if (passwordLength < 6) {
                    ScreenLoader.getInstance().displayAlert("Password Too Short", "The given password has less than 6 characters", Alert.AlertType.ERROR);
                } else if (passwordLength > 20) {
                    ScreenLoader.getInstance().displayAlert("Password Too Long", "The given password exceeded the maximum length of 20 characters", Alert.AlertType.ERROR);
                }
            }

            return false;
        }

        return true;
    }

    public static boolean validatePasswordVerification(String password, String passwordVerification, boolean showAlerts) {
        if (!password.equals(passwordVerification)) {
            if (showAlerts) {
                ScreenLoader.getInstance().displayAlert("Password Check Invalid", "The given password was not the same as the verification password", Alert.AlertType.ERROR);
            }
            return false;
        }

        return true;
    }
}
