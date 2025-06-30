$(document).ready(() => {
	const table = $("#tabel-ranking").DataTable({
		ajax: {
			url: "/api/ranking",
			dataSrc: "",
		},
		columnDefs: [
			{
				className: "text-center",
				targets: "_all",
				orderable: true,
				searchable: true,
			},
		],
		order: [[2, "desc"]],
		columns: [
			{ data: null },
			{ data: "candidateName" },
			{
				data: "totalScore",
				render: function (data) {
					return parseFloat(data).toFixed(2); // Format jadi 2 digit
				},
			},
			{ data: "rankingOrder" },
		],
	});

	// Auto-numbering
	$("#tabel-ranking").on("draw.dt", function () {
		$("#tabel-ranking")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});

	// Tombol Hitung Ranking
	$("#btnCalculateRanking").on("click", () => {
		Swal.fire({
			title: "Yakin ingin menghitung ulang nilai akhir dan peringkat?",
			text: "Data peringkat akan diperbarui berdasarkan skor terbaru.",
			icon: "question",
			showCancelButton: true,
			confirmButtonText: "Ya, Hitung!",
			cancelButtonText: "Batal",
			confirmButtonColor: "#0d6efd",
		}).then((result) => {
			if (result.isConfirmed) {
				$.ajax({
					url: "/api/ranking/calculate",
					beforeSend: initializeCSRFToken(),
					type: "POST",
					success: () => {
						Swal.fire({
							icon: "success",
							title: "<h4 class='fw-bold text-success'>Berhasil!</h4>",
							html: "<div class='mt-2'>Perhitungan nilai akhir dan peringkat telah diperbarui.</div>",
							showConfirmButton: false,
							timer: 2000,
							timerProgressBar: true,
							position: "center",
							background: "#e9fbe6",
						});
						$("#tabel-ranking").DataTable().ajax.reload();
					},
					error: () => {
						Swal.fire({
							icon: "error",
							title: "Gagal",
							text: "Terjadi kesalahan saat menghitung ranking.",
						});
					},
				});
			}
		});
	});

	// Tombol Cetak PDF
	$("#btnExportRankingPDF").on("click", () => {
		window.open("/api/ranking/export", "_blank");
	});
});
