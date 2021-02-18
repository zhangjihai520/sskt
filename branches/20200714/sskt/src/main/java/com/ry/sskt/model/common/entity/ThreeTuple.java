package com.ry.sskt.model.common.entity;

/**
 * 封装数组的操作,防止越界等
 *
 * @param <A>
 * @author 幸仁强
 */
public class ThreeTuple<A, B, C> {

    public final A first;

    public final B second;

    public final C third;

    public ThreeTuple(A a, B b, C c) {
        first = a;
        second = b;
        third = c;
    }

}