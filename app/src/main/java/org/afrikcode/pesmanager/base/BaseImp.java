package org.afrikcode.pesmanager.base;

public abstract class BaseImp<T> {

    public T view;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }
}
