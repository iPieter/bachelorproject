//TODO validate the form and inform the user if the form is incorrect.
$(document).ready( function()
{
    //$("#client_side_error").hide();
    console.log("Checking form.");
    /*$("#submit").click( function(event)
    {
        event.preventDefault();
        $("#client_side_error").text("");
        var amountOfErrors = 0;
        var messages = [];
        amountOfErrors += testInput(/[0-9]/g, $("#first_name").val(), "First name can not contain numbers.", messages );
        amountOfErrors += testInput(/[0-9]/g, $("#last_name").val(), "Last name can not contain numbers.", messages );
        amountOfErrors += testInput(/[0-9]/g, $("#city").val(), "City can not contain numbers.", messages );
        amountOfErrors += testInput(/[0-9]/g, $("#country").val(), "Country can not contain numbers.", messages );
        if( amountOfErrors > 0 )
        {
            event.preventDefault();
            for( var i = 0; i < messages.length; i++ )
                addError("client_side_error", messages[i] );
        }
    });*/

});

function testInput(regExp, value, error, messages )
{
    var errorValue = 0;
    if( regExp.test( value ))
    {
        messages.push( error );
        errorValue++;
    }
    if( value === "" )
    {
        messages.push( "Field can not be empty" );
        errorValue++;
    }
    return errorValue;
}

function addError(id,error)
{
    $("#" + id).append("<div class=\"alert alert-danger\">"+ error + "</div>");
}
