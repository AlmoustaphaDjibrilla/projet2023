package com.adi.projet2023.model.Piece;

import java.io.Serializable;

public class Cuisine extends Piece implements Serializable {
    public Cuisine(){

    }
    public Cuisine(String nom){
        super(TypePiece.CUISINE.toString(), nom);
    }
}
