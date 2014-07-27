/**
 * 
 */
$(document).ready(function() {
	$('.jquery').each(function() {
		eval($(this).html());
	});
	
	$("#order").hide();
	$("#open_orders_tab").show();
	$("#delayed_orders_tab").hide();
	$("#clients_in_debt_tab").hide();
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
	
	$("#delayed_orders_tab_link").click(function() {
		if ($("#delayed_orders_tab").is(":visible")) {
			$("#delayed_orders_tab").show();
		} else {
			$("#delayed_orders_tab").show();
			$("#open_orders_tab").hide();
			$("#clients_in_debt_tab").hide();
		}
	})
	
	$("#open_orders_tab_link").click(function() {
		if ($("#open_orders_tab").is(":visible")) {
			$("#open_orders_tab").show();
		} else {
			$("#open_orders_tab").show();
			$("#delayed_orders_tab").hide();
			$("#clients_in_debt_tab").hide();
		}
	})
	
	$("#clients_in_debt_tab_link").click(function() {
		if ($("#clients_in_debt_tab").is(":visible")) {
			$("#clients_in_debt_tab").show();
		} else {
			$("#clients_in_debt_tab").show();
			$("#delayed_orders_tab").hide();
			$("#open_orders_tab").hide();
		}
	})
});