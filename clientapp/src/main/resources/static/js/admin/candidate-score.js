$(document).ready(() => {
	$("#tabel-skor-kandidat").DataTable({
		ajax: {
			url: "/api/candidate-score",
			dataSrc: "",
		},
		columnDefs: [
			{
				className: "text-center",
				targets: "_all",
				searchable: true,
				orderable: true,
			},
		],
		order: [[1, "asc"]],
		columns: [
			{ data: "id" },
			{ data: "candidateName" },
			{ data: "subcriteriaCode" },
			{ data: "score" },
			{
				data: null,
				render: (data) => {
					return /*html*/ `
                    <div class="d-flex m-auto gap-4 justify-content-center">
						<button
                            type="button"
                            class="btn btn-primary btn-sm align-items-center"
                            data-toggle="modal"
                            data-target="#modalDetailSkorKandidat"
                            title="Detail ${data.subcriteriaCode}"
                            onclick="findCandidateScoreById(${data.id})">
                            <span class="material-symbols-rounded"> info </span>
                        </button> 
                        <button
                            type="button"
                            class="btn btn-danger d-flex align-items-center"
                            title="Hapus ${data.subcriteriaCode}"
                            onclick="deleteCandidateScore(${data.id}, \`${data.subcriteriaCode}\`)">
                            <span class="material-symbols-rounded"> delete </span>
                        </button>   
                    </div>`;
				},
			},
		],
	});

	$("#tabel-skor-kandidat").on("draw.dt", function () {
		$("#tabel-skor-kandidat")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});

	$("#modalTambahSkorKandidat").on("show.bs.modal", function () {
		loadCandidateOptions();
		loadSubcriteriaOptions();
	});

	$("#select-kandidat").on("change", function () {
		const candidateId = $(this).val();
		if (!candidateId) return;

		// Clear semua input skor dulu
		$("#form-skor-subkriteria input[name='score']").val("");

		// Ambil skor dari API
		$.ajax({
			url: `/api/candidate-score/candidate/${candidateId}`,
			method: "GET",
			success: function (data) {
				// Loop hasil dari API
				data.forEach((item) => {
					// Temukan input yang punya subcriteriaId sesuai data
					const input = $(
						`#form-skor-subkriteria input[data-subcriteria-id='${item.subcriteriaId}']`
					);
					if (input.length) {
						input.val(item.score); // Isi nilainya
					}
				});
			},
			error: function (xhr, status, error) {
				console.error("Gagal mengambil data skor kandidat:", error);
			},
		});
	});
});

// Submit form tambah skor kandidat
$("#formTambahSkorKandidat").on("submit", function (e) {
	e.preventDefault();

	const candidateId = $("#select-kandidat").val();
	if (!candidateId) {
		Swal.fire(
			"Peringatan",
			"Silakan pilih calon kader terlebih dahulu.",
			"warning"
		);
		return;
	}

	const scores = [];
	$(".input-skor-subkriteria").each(function () {
		const subcriteriaId = $(this).data("subcriteria-id");
		const score = parseFloat($(this).val());
		if (!isNaN(score)) {
			scores.push({ subcriteriaId, score });
		}
	});

	if (scores.length === 0) {
		Swal.fire("Peringatan", "Minimal isi satu penilaian!", "warning");
		return;
	}

	$.ajax({
		url: "/api/candidate-score",
		method: "POST",
		contentType: "application/json",
		dataType: "text",
		data: JSON.stringify({ candidateId, scores }),
		beforeSend: initializeCSRFToken(),
		success: () => {
			Swal.fire({
				icon: "success",
				title: "<h4 class='fw-bold text-success'>Berhasil!</h4>",
				html: "<div class='mt-2'>Penilaian calon kader berhasil ditambahkan atau diubah ke dalam sistem.</div>",
				showConfirmButton: false,
				timer: 2000,
				timerProgressBar: true,
				position: "center",
				background: "#e9fbe6",
			});
			$("#modalTambahSkorKandidat").modal("hide");
			$("#formTambahSkorKandidat")[0].reset();
			$("#tabel-skor-kandidat").DataTable().ajax.reload();
		},
		error: (xhr) => {
			const msg =
				xhr.responseJSON?.message ||
				"Terjadi kesalahan saat menambahkan penilaian calon kader.";
			Swal.fire({
				icon: "error",
				title: "<h4 class='fw-bold text-danger'>Gagal Menambahkan!</h4>",
				html: `<div class='mt-2'>${msg}</div>`,
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				background: "#fdeaea",
			});
		},
	});
});

// Load kandidat ke select option
function loadCandidateOptions() {
	$.get("/api/candidate", function (data) {
		const select = $("#select-kandidat");
		select.empty().append('<option value="">-- Pilih Calon Kader --</option>');
		data.forEach((k) => {
			select.append(`<option value="${k.id}">${k.name}</option>`);
		});
	});
}

// Load seluruh subkriteria dan generate input skornya
function loadSubcriteriaOptions() {
	$.get("/api/subcriteria", function (data) {
		const container = $("#form-skor-subkriteria");
		container.empty();

		if (data.length === 0) {
			container.append(
				"<p class='text-muted'>Belum ada subkriteria tersedia.</p>"
			);
			return;
		}

		data.forEach((sub) => {
			container.append(`
        <div class="mb-3">
        <label class="form-label fw-semibold">${sub.code} - ${sub.description}</label>
        <input type="number" min="1" max="5" class="form-control input-skor-subkriteria"
			data-subcriteria-id="${sub.id}" name="score" placeholder="Masukkan skor (1—5)"
			title="1 = Tidak sesuai, 2 = Kurang sesuai, 3 = Cukup sesuai, 4 = Sesuai, 5 = Sangat sesuai" />
        <div class="invalid-feedback">Skor harus antara 1 sampai 5.</div>
        </div>
    `);
		});
	});
}

// Export to PDF
$("#printPDFCandidateScore").on("click", () => {
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

	fetch("/api/candidate-score/export")
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

// Get CandidateScore By ID
function findCandidateScoreById(id) {
	$.ajax({
		url: `/api/candidate-score/${id}`,
		type: "GET",
		contentType: "application/json",
		success: function (response) {
			$("#candidate-score-id").text(response.id);
			$("#candidate-score-name").text(response.candidateName);
			$("#candidate-score-subcriteriaCode").text(response.subcriteriaCode);
			$("#candidate-score-subcriteriaDescription").text(
				response.subcriteriaDescription
			);
			$("#candidate-score-score").text(response.score);
			$("#modalDetailSkorKandidat").modal("show");
		},
		error: function (xhr, status, error) {
			console.log(error);
			Swal.fire({
				icon: "error",
				title:
					"<h4 class='fw-bold text-danger'>Gagal Menampilkan Data Penilaian Calon Kader!</h4>",
				html: "<div class='mt-2'>Terjadi kesalahan saat menampilkan data penilaian calon kader. Silakan coba lagi.</div>",
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
}

// Delete Candidate Score
function deleteCandidateScore(id, subcriteriaCode) {
	Swal.fire({
		title: `<h4 class="fw-bold">Hapus Penilaian Calon Kader <span class="text-danger">${subcriteriaCode}</span>?</h4>`,
		html: "<div class='mt-2'>Tindakan ini tidak dapat dibatalkan.</div>",
		icon: "warning",
		showCancelButton: true,
		confirmButtonText: "Ya, Hapus!",
		cancelButtonText: "Batal",
		confirmButtonColor: "#dc3545",
		cancelButtonColor: "#6c757d",
		background: "#fff8e6",
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				type: "DELETE",
				url: `/api/candidate-score/${id}`,
				dataType: "JSON",
				beforeSend: initializeCSRFToken(),
				contentType: "application/json",
				success: () => {
					$("#tabel-skor-kandidat").DataTable().ajax.reload();
					Swal.fire({
						icon: "success",
						title: "<h4 class='fw-bold text-success'>Berhasil Dihapus!</h4>",
						html: `<div class='mt-2'>Penilaian calon kader <strong>${subcriteriaCode}</strong> berhasil dihapus.</div>`,
						showConfirmButton: false,
						timer: 1500,
						timerProgressBar: true,
						position: "center",
						background: "#e9fbe6",
					});
				},
				error: (xhr) => {
					console.error(xhr);
					Swal.fire({
						icon: "error",
						title: "<h4 class='fw-bold text-danger'>Gagal Menghapus!</h4>",
						html: `<div class='mt-2'>Tidak dapat menghapus penilaian calon kader <strong>${subcriteriaCode}</strong>. Silakan coba lagi.</div>`,
						confirmButtonText: "Oke",
						confirmButtonColor: "#dc3545",
						position: "center",
						background: "#fdeaea",
					});
				},
			});
		}
	});
}
