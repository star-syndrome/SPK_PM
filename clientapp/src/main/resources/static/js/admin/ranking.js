$(document).ready(() => {
	$("#tabel-ranking").DataTable({
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

	// Export to PDF
	$("#btnExportRankingPDF").on("click", () => {
		Swal.fire({
			title: "Mohon tunggu...",
			text: "Sedang menyiapkan laporan PDF",
			timerProgressBar: true,
			allowOutsideClick: false,
			showConfirmButton: false,
			didOpen: () => {
				Swal.showLoading();
			},
		});

		fetch("/api/ranking/export")
			.then((res) => {
				if (!res.ok) throw new Error("Gagal mencetak laporan.");

				// Ambil nama file dari Content-Disposition
				const contentDisposition = res.headers.get("Content-Disposition");
				let filename = "laporan.pdf"; // default

				if (contentDisposition && contentDisposition.includes("filename=")) {
					filename = contentDisposition
						.split("filename=")[1]
						.replaceAll('"', "")
						.trim();
				}

				return res.blob().then((blob) => ({ blob, filename }));
			})
			.then(({ blob, filename }) => {
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement("a");
				a.href = url;
				a.download = filename;
				document.body.appendChild(a);
				a.click();
				document.body.removeChild(a);
				window.URL.revokeObjectURL(url);

				// Tampilkan notifikasi
				Swal.fire({
					icon: "success",
					title: "Berhasil",
					text: "Laporan berhasil diunduh!",
					timer: 2000,
					showConfirmButton: false,
				});
			})
			.catch((err) => {
				Swal.fire({
					icon: "error",
					title: "Gagal",
					text: err.message || "Terjadi kesalahan saat mencetak laporan.",
				});
			});
	});
});
