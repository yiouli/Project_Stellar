package org.yiouli.common;

public class NullCommand<T> implements ICommand<T> {

	@Override
	public boolean execute(T obj) {
		return true;
	}
}
