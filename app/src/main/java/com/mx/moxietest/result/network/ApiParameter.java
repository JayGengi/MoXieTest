package com.mx.moxietest.result.network;

public class ApiParameter {
    public final String name;
    public final Object value;

    ApiParameter(String name, Object value) throws IllegalArgumentException {
        if (name != null) {
            name = name.trim();
        }
        if (name == null || name.length() <= 0)
            throw new IllegalArgumentException("The argument 'name' can NOT be NULL or BLANK.");

        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ApiParameter{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
