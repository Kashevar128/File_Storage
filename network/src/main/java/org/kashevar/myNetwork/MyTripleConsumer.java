package org.kashevar.myNetwork;

public interface MyTripleConsumer<T,V,K> {
    void accept(T t,V v, K k);
}
