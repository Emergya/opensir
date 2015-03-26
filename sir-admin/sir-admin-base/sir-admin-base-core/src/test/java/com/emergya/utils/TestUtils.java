/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emergya.utils;

import java.util.Iterator;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
public class TestUtils {

	public static <T extends Comparable<? super T>> boolean isSortedAsc(
			Iterable<T> iterable) {
		Iterator<T> iter = iterable.iterator();
		if (!iter.hasNext()) {
			return true;
		}
		T t = iter.next();
		while (iter.hasNext()) {
			T t2 = iter.next();
			if (t.compareTo(t2) >= 0) {
				return false;
			}
			t = t2;
		}
		return true;
	}

	public static <T extends Comparable<? super T>> boolean isSortedDesc(
			Iterable<T> iterable) {
		Iterator<T> iter = iterable.iterator();
		if (!iter.hasNext()) {
			return true;
		}
		T t = iter.next();
		while (iter.hasNext()) {
			T t2 = iter.next();
			if (t2.compareTo(t) >= 0) {
				return false;
			}
			t = t2;
		}
		return true;
	}
}
