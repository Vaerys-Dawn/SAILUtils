package com.github.vaerys.sailutils.databaseutils;

import discord4j.store.Store;
import discord4j.store.primitive.LongObjStore;
import discord4j.store.service.StoreService;

import java.io.Serializable;

public class DataHandler implements StoreService {

    @Override
    public boolean hasGenericStores() {
        return false;
    }

    @Override
    public <K extends Comparable<K>, V extends Serializable> Store<K, V> provideGenericStore(Class<K> aClass, Class<V> aClass1) {
        return null;
    }

    @Override
    public boolean hasLongObjStores() {
        return false;
    }

    @Override
    public <V extends Serializable> LongObjStore<V> provideLongObjStore(Class<V> aClass) {
        return null;
    }
}
