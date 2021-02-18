package com.ry.sskt.model.common.entity;

/**
 * 封装数组的操作,防止越界等
 * 
 * @author 幸仁强
 * @param <A>
 */
public class TwoTuple<A, B>
{

    public final A first;

    public final B second;

    public TwoTuple(A a, B b)
    {
        first = a;
        second = b;
    }

}