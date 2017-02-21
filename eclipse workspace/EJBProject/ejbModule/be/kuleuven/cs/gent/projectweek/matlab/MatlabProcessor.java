package be.kuleuven.cs.gent.projectweek.matlab;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

@Singleton
@Startup
public class MatlabProcessor
{
	String script = "";
	
	@PostConstruct
	public void init() throws MatlabConnectionException, MatlabInvocationException, IOException
	{
		MatlabProxyFactory factory = new MatlabProxyFactory(); 
		MatlabProxy proxy = factory.getProxy();

		//Display 'hello world' just like when using the demo
		proxy.eval("disp('hello world')");

		//Disconnect the proxy from MATLAB
		proxy.disconnect();
	}
}
