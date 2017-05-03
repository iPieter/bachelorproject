var fs      = require( 'fs' );
var request = require( 'request' );

var data    = JSON.parse( fs.readFileSync( 'test.json', 'utf8' ) );

var index   = 700;
var end = 100 + index;
var baseURL = "http://ec2-54-202-94-106.us-west-2.compute.amazonaws.com:8080/DWPProject/rest";

var beginStation = "Oostende";
var endStation = "Luik";

request( baseURL + '/live_data/register/M8/15921/Bombardier/' + beginStation + "-" + endStation, function( error, response, body )
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

        if( index >= end || index > data.yaw.length )
        {
            request( baseURL + '/live_data/stop/' + lsdID, function( error, response, body )
            {
                clearInterval( timer );
            } );
        }
    }, 250 );
});
