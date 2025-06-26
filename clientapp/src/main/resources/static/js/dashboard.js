$(document).ready(function () {
	// Ambil total kriteria
	$.ajax({
		type: "GET",
		url: "/api/criteria/total",
		success: function (data) {
			$("#total-criteria").text(data);
		},
		error: function (err) {
			console.error("Gagal ambil total kriteria", err);
		},
	});

	// Ambil total subkriteria
	$.ajax({
		type: "GET",
		url: "/api/subcriteria/total",
		success: function (data) {
			$("#total-subcriteria").text(data);
		},
		error: function (err) {
			console.error("Gagal ambil total subkriteria", err);
		},
	});

	// Ambil total kandidat
	$.ajax({
		type: "GET",
		url: "/api/candidate/total",
		success: function (data) {
			$("#total-candidate").text(data);
		},
		error: function (err) {
			console.error("Gagal ambil total kandidat", err);
		},
	});
});
