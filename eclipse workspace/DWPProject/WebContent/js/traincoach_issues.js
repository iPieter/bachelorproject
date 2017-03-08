function openModal( id )
{
	console.log( id );
	$( '#modal_form\\:modal_hidden_input' ).val( id );
	$( "#assignModal" ).modal();
}