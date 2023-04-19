package com.adi.projet2023.model.Piece;

import java.io.Serializable;

public class Douche extends Piece implements Serializable {
    public Douche(){

    }
    public Douche(String nom){
        super(TypePiece.DOUCHE.toString(), nom);
    }
}
