package com.adi.projet2023.model.Piece;

import java.io.Serializable;

public class Autre extends Piece implements Serializable {
    public Autre(){

    }
    public Autre(String nom){
        super(TypePiece.AUTRE, nom);
    }

}
