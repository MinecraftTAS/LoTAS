package de.pfannekuchen.lotas.core.utils;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings(value = { "unchecked", "rawtypes" })
/**
 * List that has a default value that is not null
 */
public class DefaultedList<E> extends AbstractList<E> {
	private final List<E> delegate;
	private final E initialElement;

	public static <E> DefaultedList<E> ofSize(int size, E defaultValue) {
		Validate.notNull(defaultValue);
		Object[] objects = new Object[size];
		Arrays.fill(objects, defaultValue);
		return new DefaultedList(Arrays.asList(objects), defaultValue);
	}

	@SafeVarargs
	public static <E> DefaultedList<E> copyOf(E defaultValue, E... values) {
		return new DefaultedList(Arrays.asList(values), defaultValue);
	}

	protected DefaultedList(List<E> delegate, @Nullable E initialElement) {
		this.delegate = delegate;
		this.initialElement = initialElement;
	}

	@NotNull
	public E get(int index) {
		return this.delegate.get(index);
	}

	public E set(int index, E element) {
		Validate.notNull(element);
		return this.delegate.set(index, element);
	}

	public void add(int value, E element) {
		Validate.notNull(element);
		this.delegate.add(value, element);
	}

	public E remove(int index) {
		return this.delegate.remove(index);
	}

	public int size() {
		return this.delegate.size();
	}

	public void clear() {
		if (this.initialElement == null) {
			super.clear();
		} else {
			for (int i = 0; i < this.size(); ++i) {
				this.set(i, this.initialElement);
			}
		}

	}
}