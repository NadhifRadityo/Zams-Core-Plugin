package io.github.NadhifRadityo.ZamsNetwork.Core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingInvocationHandler implements InvocationHandler {
	private final Object delegate;
	public LoggingInvocationHandler(final Object delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("method: " + method + ", args: " + args);
		return method.invoke(delegate, args);
	}
}

