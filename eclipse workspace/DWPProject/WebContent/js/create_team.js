$( document ).ready( function()
{
	$( "#submit" ).click( function( event )
	{
		$( ".sport_input" ).each( function( e )
		{
			if (  $(this).is( ":checked" ) )
                $( "#final_sport_input" ).val( $( "#final_sport_input" ).val() + (  $(this).attr("name") ) + "," );
		} );

        $( ".friend_input" ).each( function( e )
		{
			if (  $(this).is( ":checked" ) )
                $( "#final_friend_input" ).val( $( "#final_friend_input" ).val() + (  $(this).attr("name") ) + "," );
		} );

        $( "#final_sport_input" ).val( $( "#final_sport_input" ).val( ).substr(0, $( "#final_sport_input" ).val( ).length - 1 ));
        $( "#final_friend_input" ).val( $( "#final_friend_input" ).val( ).substr(0, $( "#final_friend_input" ).val( ).length - 1 ));
	} );
} );
