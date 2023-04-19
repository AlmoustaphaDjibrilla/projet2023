package com.adi.projet2023.model.Piece;

import java.io.Serializable;

public class Chambre extends Piece implements Serializable {
    public Chambre(){

    }
    public Chambre(String nom){
        super(TypePiece.CHAMBRE.toString(), nom);
    }

}
