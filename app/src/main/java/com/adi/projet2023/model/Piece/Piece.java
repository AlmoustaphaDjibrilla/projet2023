package com.adi.projet2023.model.Piece;

import java.io.Serializable;

public abstract class Piece implements Serializable {
    String idPiece;
    String nomPiece;
    TypePieces typePiece;

    public Piece(){
    }
    public Piece( TypePieces type, String nom){
        this.typePiece=type;
        this.nomPiece=nom;
    }
    public void setIdPiece(String idPiece){
        this.idPiece =idPiece;
    }

    public String getIdPiece() {
        return idPiece;
    }

    public void setTypePiece(TypePieces typePiece) {
        this.typePiece = typePiece;
    }

    public TypePieces getTypePiece() {
        return typePiece;
    }

    public void setNomPiece(String nomPiece) {
        this.nomPiece = nomPiece;
    }

    public String getNomPiece() {
        return nomPiece;
    }
}
