var fs      = require( 'fs' );
var request = require( 'request' );

var data    = JSON.parse( fs.readFileSync( 'test.json', 'utf8' ) );

var index   = 700;
var baseURL = "http://10.108.0.121:8080/DWPProject-0.0.1-SNAPSHOT/rest";

request( baseURL + '/live_data/register/M8/15921/Bombardier/Luik-Oostende', function( error, response, body )
{
    if( error !== null )
        console.log( error );
    console.log( response.statusCode , body );

    var lsdID = body;
    
    var timer   = setInterval( function( )
    {
        var adjustedIndex = Math.round( index / data.yaw.length * data.speed.length );
        index++;

        var content = lsdID + "/";
        content += data.lat[ adjustedIndex ] + "/";
        content += data.lng[ adjustedIndex ] + "/";
        content += data.speed[ adjustedIndex ] + "/";
        content += data.accel[ adjustedIndex ] + "/";
        content += data.yaw[ index ] + "/";
        content += data.roll[ index ];

        console.log( content );

        request.post( baseURL + '/live_data/add_entry/' + content, function( error, response, body )
        {
        });

        if( index >= 750 || index > data.yaw.length )
        {
            request( baseURL + '/live_data/stop/' + lsdID, function( error, response, body )
            {
                clearInterval( timer );
            } );
        }
    }, 250 );
});
