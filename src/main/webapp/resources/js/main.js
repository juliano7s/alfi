/**
 * 
 */
$(document).ready(function() {
	$('.jquery').each(function() {
		eval($(this).html());
	});
});

$(function() {
	$(".datepicker").datepicker({
		dateFormat : "dd/mm/yy"
	});

	$("#hide_order").click(function() {
		if ($("#new_order").is(":visible")) {
			$("#new_order").slideUp("fast");
		} else {
			$("#new_order").slideDown("fast");
		}
	});
});