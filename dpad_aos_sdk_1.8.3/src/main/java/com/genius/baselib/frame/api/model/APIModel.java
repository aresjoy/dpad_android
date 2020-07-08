package com.genius.baselib.frame.api.model;

import android.content.Context;

import com.sera.volleyhelper.base.JsonParseUtil;

import org.json.JSONObject;

/**
 * Created by hongseok on 2017-02-01.
 */

public abstract class APIModel<T,T1> extends JsonParseUtil {

    public abstract JSONObject getHeaders(T1 t1);
    public abstract void parse(T tBaseJsonApi, Context context, JSONObject response);
}
