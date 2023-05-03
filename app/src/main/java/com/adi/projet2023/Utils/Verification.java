package com.adi.projet2023.Utils;

import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.local.Local;

public class Verification {
    public interface OnValueReceivedListener<T> {
        void onValueReceived(T value);
    }

    public static <T> void verifier_nom_local(String nom, Class<T> valueType, final Verification.OnValueReceivedListener<T> listener) {

    }

    public static <T> void verifier_nom_piece(String nom, Local local, Class<T> valueType, final Verification.OnValueReceivedListener<T> listener) {

    }

    public static <T> void verifier_nom_composant(String nom, Local local, Piece piece, Class<T> valueType, final Verification.OnValueReceivedListener<T> listener) {

    }
}
