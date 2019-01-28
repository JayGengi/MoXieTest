package com.mx.moxietest.result.network;

import java.util.ArrayList;
import java.util.List;

public class ApiParameterList extends ArrayList<ApiParameter> {
    private static final long serialVersionUID = 3668948424416187047L;

    private ApiParameterList() {
    }

    public final Boolean add(String name, Object value) throws IllegalArgumentException {
        ApiParameter parameter = new ApiParameter(name, value);
        return this.add(parameter);
    }

    public final ApiParameterList with(String name, Object value) throws IllegalArgumentException {
        this.add(name, value);
        return this;
    }

    public void remove(String name) {
        for (ApiParameter item : this) {
            if (item.name.equals(name)) {
                this.remove(item);
                break;
            }
        }
    }

    public void removeContains(String name) {
        List<ApiParameter> delete = new ArrayList<ApiParameter>();
        for (ApiParameter item : this) {
            if (item.name.startsWith(name)) {
                delete.add(item);
            }
        }
        for (ApiParameter di : delete) {
            this.remove(di);
        }
    }

    public Object getValue(String name) {
        Object ret = null;
        for (ApiParameter item : this) {
            if (item.name.equals(name)) {
                ret = item.value;
                break;
            }
        }

        return ret;
    }

    public final static ApiParameterList create() {
        return new ApiParameterList();
    }

    public final static ApiParameterList createWith(String name, Object value) throws IllegalArgumentException {
        ApiParameterList list = new ApiParameterList();
        return list.with(name, value);
    }

    public final boolean contains(String name) {
        for (ApiParameter item : this) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
