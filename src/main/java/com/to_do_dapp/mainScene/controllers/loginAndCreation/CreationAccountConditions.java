package com.to_do_dapp.mainScene.controllers.loginAndCreation;

public class CreationAccountConditions {
    public static String emailSyntax(String email) {
        if (email.length() > 50) {
            return "El correo no debe exceder los 50 caracteres";
        }

        if (!email.contains("@")) {
            return "Su correo debe tener la siguiente sintaxis usuario@dominio";
        }

        String emailLowerCase = email.toLowerCase();
        if (!email.equals(emailLowerCase)) {
            return "EL email no puede contener mayusculas";
        }

        boolean correctlyEnded = false;
        if (email.endsWith(".com")) {
            correctlyEnded = true;
        } else if (email.endsWith(".org")) {
            correctlyEnded = true;
        } else if (email.endsWith(".es")) {
            correctlyEnded = true;
        } else if (email.endsWith(".dev")) {
            correctlyEnded = true;
        } else {
            correctlyEnded = false;
        }

        if (!correctlyEnded) {
            return "El correo debe terminar en .com, .org, .es o .dev";
        }

        if (email.isBlank()) {
            return "Comprueba que el email no esta vacio";
        }
        
        for (char c : email.toCharArray()) {
            if (c != 64 && c != 46) {
                if (c < 48 || (c > 47 && c < 65) || (c > 90 && c < 96) || c > 122) {
                    return "Email invalido, compruebe los caracteres";
                }
            }
        }

        return "";
    }

    public static String usernameSyntax(String username) {
        if (username.length() > 20) {
            return "El usuario no puede contener ma de 20 caracteres";
        }

        if (username.isBlank()) {
            return "Comprueba que el nombre de usuario no esta vacio";
        }

        for (char c : username.toCharArray()) {
            if (c < 48 || (c > 47 && c < 65) || (c > 90 && c < 96) || c > 122) {
                return "Solo se permiten numeros y letras";
            }
        }

        return "";
    }

    public static String paswordSyntax(String password) {
        if (password.isBlank()) {
            return "La contraseña no puede estar vacia";
        }

        return "";
    }
}
