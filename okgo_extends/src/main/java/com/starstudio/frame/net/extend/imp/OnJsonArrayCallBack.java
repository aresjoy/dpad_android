package com.starstudio.frame.net.extend.imp;

/**
 * Created by hongsec on 17. 7. 4.
 */

public interface OnJsonArrayCallBack<T> {

    /**
     *
     * @param position
     * @param itemjsonObject  Maybe string, jsonobject or other.
     * @return
     */
    public T getItem(int position, Object itemjsonObject);
}
