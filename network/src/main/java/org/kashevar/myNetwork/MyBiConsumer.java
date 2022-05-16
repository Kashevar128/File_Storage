package org.kashevar.myNetwork;

public interface MyBiConsumer<T,V> {
    void accept(T t,V v);
}
