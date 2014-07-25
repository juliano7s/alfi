/**
 * 
 */
$(document).ready(function() {
	$('.jquery').each(function() {
		eval($(this).html());
	});
	
	$("#order").hide();
});

$(function() {
	$(".datepicker").datepicker({
		dateFormat : "dd/mm/yy"
	});	

	$("#hide_order").click(function() {
		if ($("#order").is(":visible")) {
			$("#order").slideUp("fast");
		} else {
			$("#order").slideDown("fast");
		}
	});
});