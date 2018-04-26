package br.com.portalautocenter.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.portalautocenter.models.Abastecimento;

/**
 * Created by 16254855 on 19/04/2018.
 */

@Dao
public interface AbastecimentoDao {
    @Query("SELECT * FROM abastecimento")
    List<Abastecimento> getAll();

    @Query("SELECT * FROM abastecimento WHERE idUsuario IN (:idU) AND idVeiculo IN(:idV)")
    List<Abastecimento> getByUser(int idU, int idV);

    @Insert
    void insertAll(Abastecimento... abastecimentos);

    @Delete
    void delete(Abastecimento... abastecimentos);
}
