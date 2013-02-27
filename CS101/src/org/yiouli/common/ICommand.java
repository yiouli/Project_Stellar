package org.yiouli.common;

public interface ICommand<T> {

	public boolean execute(T obj);
}
