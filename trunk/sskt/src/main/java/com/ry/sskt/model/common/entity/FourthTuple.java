package com.ry.sskt.model.common.entity;

/**
 * 封装数组的操作,防止越界等
 *
 * @param <A>
 * @author 幸仁强
 */
public class FourthTuple<A, B, C, D> {

    public final A first;

    public final B second;

    public final C third;

    public final D fourth;

    public FourthTuple(A a, B b, C c, D d) {
        first = a;
        second = b;
        third = c;
        fourth = d;
    }

}