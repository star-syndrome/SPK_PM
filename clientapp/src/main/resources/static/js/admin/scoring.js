$(document).ready(() => {
	loadGapConversionTable();
	loadGapTable();
	loadCFSFTable();
	loadFinalScoreDetailTable();
	loadFinalScoreTable();
});

function loadGapConversionTable() {
	if ($.fn.DataTable.isDataTable("#gap-conversion-table")) {
		$("#gap-conversion-table").DataTable().clear().destroy();
	}

	$.ajax({
		url: "/api/candidate-score/gap-conversion",
		method: "GET",
		success: function (data) {
			const gapTable = $("#gap-conversion-table").DataTable({
				data: data,
				columns: [
					{
						data: null,
						render: function (data, type, row, meta) {
							return meta.row + 1;
						},
					},
					{ data: "gap" },
					{ data: "bobotNilai" },
					{ data: "keterangan" },
				],
				columnDefs: [
					{
						className: "text-center",
						targets: "_all",
					},
				],
				order: [[1, "asc"]],
			});
		},
		error: function (xhr) {
			console.error("Gagal mengambil data gap conversion:", xhr.responseText);
		},
	});

	$("#gap-conversion-table").on("draw.dt", function () {
		$("#gap-conversion-table")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});
}

function loadGapTable() {
	if ($.fn.DataTable.isDataTable("#gap-table")) {
		$("#gap-table").DataTable().clear().destroy();
	}

	$.ajax({
		url: "/api/candidate-score/gap",
		method: "GET",
		success: function (data) {
			// Inisialisasi DataTable
			const gapTable = $("#gap-table").DataTable({
				data: data,
				columns: [
					{
						data: null,
						render: function (data, type, row, meta) {
							return meta.row + 1;
						},
					},
					{ data: "candidateName" },
					{ data: "criteriaName" },
					{ data: "subcriteriaCode" },
					{ data: "score" },
					{ data: "target" },
					{ data: "gap" },
					{ data: "convertedValue" },
				],
				columnDefs: [
					{
						className: "text-center",
						targets: "_all",
					},
				],
				order: [[1, "asc"]],
			});
		},
		error: function (xhr) {
			console.error("Gagal mengambil data gap:", xhr.responseText);
		},
	});

	$("#gap-table").on("draw.dt", function () {
		$("#gap-table")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});
}

function loadCFSFTable() {
	if ($.fn.DataTable.isDataTable("#cfsf-table")) {
		$("#cfsf-table").DataTable().clear().destroy();
	}

	$.ajax({
		url: "/api/candidate-score/cf-sf",
		method: "GET",
		success: function (data) {
			const gapTable = $("#cfsf-table").DataTable({
				data: data,
				columns: [
					{
						data: null,
						render: function (data, type, row, meta) {
							return meta.row + 1;
						},
					},
					{ data: "candidateName" },
					{ data: "criteriaCode" },
					{ data: "cf" },
					{ data: "sf" },
					{
						data: "finalScore",
						render: function (data) {
							return parseFloat(data).toFixed(2);
						},
					},
				],
				columnDefs: [
					{
						className: "text-center",
						targets: "_all",
					},
				],
				order: [[1, "asc"]],
			});
		},
		error: function (xhr) {
			console.error(
				"Gagal mengambil data core factor dan secondary factor:",
				xhr.responseText
			);
		},
	});

	$("#cfsf-table").on("draw.dt", function () {
		$("#cfsf-table")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});
}

function loadFinalScoreDetailTable() {
	if ($.fn.DataTable.isDataTable("#finalScoreDetail-table")) {
		$("#finalScoreDetail-table").DataTable().clear().destroy();
	}

	$.ajax({
		url: "/api/candidate-score/final-score-detail",
		method: "GET",
		success: function (data) {
			// Inisialisasi DataTable
			const gapTable = $("#finalScoreDetail-table").DataTable({
				data: data,
				columns: [
					{
						data: null,
						render: function (data, type, row, meta) {
							return meta.row + 1;
						},
					},
					{ data: "candidateName" },
					{ data: "criteriaCode" },
					{ data: "cf" },
					{ data: "sf" },
					{
						data: "finalScore",
						render: function (data) {
							return parseFloat(data).toFixed(2); // Format jadi 2 digit
						},
					},
					{ data: "weight" },
					{
						data: "finalScoreXWeight",
						render: function (data) {
							return parseFloat(data).toFixed(2); // Format jadi 2 digit
						},
					},
				],
				columnDefs: [
					{
						className: "text-center",
						targets: "_all",
					},
				],
				order: [[1, "asc"]],
			});
		},
		error: function (xhr) {
			console.error(
				"Gagal mengambil data final score details:",
				xhr.responseText
			);
		},
	});

	$("#finalScoreDetail-table").on("draw.dt", function () {
		$("#finalScoreDetail-table")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});
}

function loadFinalScoreTable() {
	if ($.fn.DataTable.isDataTable("#finalScore-table")) {
		$("#finalScore-table").DataTable().clear().destroy();
	}

	$.ajax({
		url: "/api/candidate-score/final-score",
		method: "GET",
		success: function (data) {
			// Inisialisasi DataTable
			const gapTable = $("#finalScore-table").DataTable({
				data: data,
				columns: [
					{
						data: null,
						render: function (data, type, row, meta) {
							return meta.row + 1;
						},
					},
					{ data: "candidateName" },
					{
						data: "totalFinalScore",
						render: function (data) {
							return parseFloat(data).toFixed(2); // Format jadi 2 digit
						},
					},
				],
				columnDefs: [
					{
						className: "text-center",
						targets: "_all",
					},
				],
				order: [[1, "asc"]],
			});
		},
		error: function (xhr) {
			console.error("Gagal mengambil data final score:", xhr.responseText);
		},
	});

	$("#finalScore-table").on("draw.dt", function () {
		$("#finalScore-table")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});
}
