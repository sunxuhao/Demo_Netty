package com.sunxuhao.crypto;

import java.util.LinkedList;
import java.util.List;

public class CryptoBuilder {
    protected List<Integer> elements = new LinkedList<>();

    public CryptoBuilder append(Integer type) {
        elements.add(type);
        return this;
    }

    public Crypto getInstance() {
        return new Crypto(elements);
    }
}


