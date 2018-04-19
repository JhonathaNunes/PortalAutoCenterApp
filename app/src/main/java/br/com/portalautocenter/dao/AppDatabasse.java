package br.com.portalautocenter.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.portalautocenter.models.Abastecimento;

/**
 * Created by 16254855 on 19/04/2018.
 */

@Database(entities = {Abastecimento.class}, version = 1)
public abstract class AppDatabasse extends RoomDatabase {
    public abstract Abastecimento abastecimentoDao();
}
