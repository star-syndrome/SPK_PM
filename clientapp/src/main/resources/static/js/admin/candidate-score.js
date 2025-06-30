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
                    <div class="d-flex m-auto gap-4 justify-content-center shadow-sm">
						<button
                            type="button"
                            class="btn btn-primary btn-sm align-items-center"
                            data-toggle="modal"
                            data-target="#modalDetailSkorKandidat"
                            title="Details ${data.subcriteriaCode}"
                            onclick="findCandidateScoreById(${data.id})">
                            <span class="material-symbols-rounded"> info </span>
                        </button> 
                        <button
                            type="button"
                            class="btn btn-danger d-flex align-items-center shadow-sm"
                            title="Delete ${data.subcriteriaCode}"
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
		loadKandidatOptions();
		loadSubkriteriaFields();
	});
});

// Submit form tambah skor kandidat
$("#formTambahSkorKandidat").on("submit", function (e) {
	e.preventDefault();

	const candidateId = $("#select-kandidat").val();
	if (!candidateId) {
		Swal.fire(
			"Peringatan",
			"Silakan pilih kandidat terlebih dahulu.",
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
		Swal.fire("Peringatan", "Minimal isi satu skor!", "warning");
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
				html: "<div class='mt-2'>Skor kandidat berhasil ditambahkan atau diubah ke dalam sistem.</div>",
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
				"Terjadi kesalahan saat menambahkan skor kandidat.";
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
function loadKandidatOptions() {
	$.get("/api/candidate", function (data) {
		const select = $("#select-kandidat");
		select.empty().append('<option value="">-- Pilih Kandidat --</option>');
		data.forEach((k) => {
			select.append(`<option value="${k.id}">${k.name}</option>`);
		});
	});
}

// Load seluruh subkriteria dan generate input skornya
function loadSubkriteriaFields() {
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
        <input type="number" min="1" max="5" step="1" class="form-control input-skor-subkriteria" 
            data-subcriteria-id="${sub.id}" placeholder="Masukkan skor (1—5)">
        <div class="invalid-feedback">Skor harus antara 1 sampai 5.</div>
        </div>
    `);
		});
	});
}

// Export to PDF
$("#printPDFCandidateScore").on("click", () => {
	window.open("/api/candidate-score/export", "_blank");
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
					"<h4 class='fw-bold text-danger'>Gagal Menampilkan Data Skor Kandidat!</h4>",
				html: "<div class='mt-2'>Terjadi kesalahan saat menampilkan data skor kandidat. Silakan coba lagi.</div>",
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
		title: `<h4 class="fw-bold">Hapus Skor Kandidat <span class="text-danger">${subcriteriaCode}</span>?</h4>`,
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
						html: `<div class='mt-2'>Skor Kandidat <strong>${subcriteriaCode}</strong> berhasil dihapus.</div>`,
						showConfirmButton: false,
						timer: 2000,
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
						html: `<div class='mt-2'>Tidak dapat menghapus skor kandidat <strong>${subcriteriaCode}</strong>. Silakan coba lagi.</div>`,
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
