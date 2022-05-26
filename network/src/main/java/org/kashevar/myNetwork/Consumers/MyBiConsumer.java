package org.kashevar.myNetwork.Consumers;

public interface MyBiConsumer<T,V> {
    void accept(T t,V v);
}
