package be.kuleuven.cs.gent.projectweek.matlab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

@Singleton
@Startup
public class MatlabProcessor
{
	String script = "";

	//@PostConstruct
	public void init() throws MatlabConnectionException, MatlabInvocationException, IOException
	{
		String script = "";
		try
		{
			URL url = getClass().getResource( "script.txt" );
			BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream() ) );
			String line = "";
			while ( ( line = reader.readLine() ) != null )
				script += line + System.getProperty( "line.separator" );
			reader.close();
		}
		catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}

		MatlabProxyFactory factory = new MatlabProxyFactory();
		MatlabProxy proxy = factory.getProxy();

		URL url = getClass().getResource( "test03_track16_speed40_radius125.mat" );
		String path = url.toString().replaceAll( "vfs:", "" );

		proxy.eval( "load('" + path + "')" );
		proxy.eval( script );

		
		MatlabTypeConverter processor = new MatlabTypeConverter( proxy );
		MatlabNumericArray yaw_out = processor.getNumericArray( "yaw_out" );
		MatlabNumericArray roll_out = processor.getNumericArray( "roll_out" );
		
		double yaw [][] = yaw_out.getRealArray2D();
		System.out.println( yaw.length );
		System.out.println( yaw[0].length );
		
		for( int i = 0; i < yaw.length; i++ )
		{
			//System.out.println( yaw[i][0] + "," );
		}
		
		proxy.disconnect();
	}
}
