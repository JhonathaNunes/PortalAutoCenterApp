package br.com.portalautocenter.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by 16254855 on 19/04/2018.
 */

@Entity
public class Abastecimento {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "idUsuario")
    private int idUsuario;

    @ColumnInfo(name = "idVeiculo")
    private int idVeiculo;

    @ColumnInfo(name = "posto")
    private String posto;

    @ColumnInfo(name = "latitude")
    private String latitude;

    @ColumnInfo(name = "longitude")
    private String longitude;

    @ColumnInfo(name = "data")
    private String data;

    @ColumnInfo(name = "litros")
    private Double litros;

    @ColumnInfo(name = "preco")
    private Double preco;

    @ColumnInfo(name = "tanque")
    private Double tanque;

    public Abastecimento() {
    }

    public Abastecimento(int idUsuario, int idVeiculo, String posto, String latitude, String longitude, String data, Double litros, Double preco, Double tanque) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idVeiculo = idVeiculo;
        this.posto = posto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
        this.litros = litros;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getPosto() {
        return posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getLitros() {
        return litros;
    }

    public void setLitros(Double litros) {
        this.litros = litros;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getTanque() {
        return tanque;
    }

    public void setTanque(Double tanque) {
        this.tanque = tanque;
    }
}
