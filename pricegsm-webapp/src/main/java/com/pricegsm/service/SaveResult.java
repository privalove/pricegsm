package com.pricegsm.service;

import java.io.Serializable;

public class SaveResult<T>
        implements Serializable {

    private T result;

    private boolean save;

    public SaveResult(T result, boolean save) {
        this.result = result;
        this.save = save;
    }

    public SaveResult() {
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    /**
     * @return true - save, false - merge.
     */
    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    /**
     * @return true - merge, false - save.
     */
    public boolean isMerge() {
        return !isSave();
    }
}
