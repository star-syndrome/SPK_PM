$(document).ready(() => {
	$("#tabel-subkriteria").DataTable({
		ajax: {
			url: "/api/subcriteria",
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
			{ data: "code" },
			{ data: "description" },
			{ data: "target" },
			{
				data: null,
				render: (data) => {
					return /*html*/ `
                    <div class="d-flex m-auto gap-4 justify-content-center">
						<button
                            type="button"
                            class="btn btn-primary align-items-center btn-sm"
                            data-toggle="modal"
                            data-target="#details-subcriteria"
                            title="Detail ${data.code}"
                            onclick="findSubcriteriaById(${data.id})">
                            <span class="material-symbols-rounded"> info </span>
                        </button> 
                        <button
                            type="button"
                            class="btn btn-warning d-flex align-items-center"
                            data-bs-toggle="modal"
                            data-bs-target="#update-subcriteria"
                            title="Ubah ${data.code}"
                            onclick="beforeUpdateSubcriteria(${data.id})">
                            <span class="material-symbols-rounded"> sync </span>
                        </button>
                        <button
                            type="button"
                            class="btn btn-danger d-flex align-items-center"
                            title="Hapus ${data.code}"
                            onclick="deleteSubcriteria(${data.id}, \`${data.code}\`)">
                            <span class="material-symbols-rounded"> delete </span>
                        </button>   
                    </div>`;
				},
			},
		],
	});

	$("#tabel-subkriteria").on("draw.dt", function () {
		$("#tabel-subkriteria")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});

	loadCriteriaOptions();
});

function loadCriteriaOptions() {
	$.ajax({
		url: "/api/criteria",
		type: "GET",
		success: function (data) {
			const select = $("#criteriaId");
			select.empty(); // kosongkan dulu
			select.append(`<option value="">-- Pilih Kriteria --</option>`);

			data.forEach((criteria) => {
				select.append(
					`<option value="${criteria.id}">${criteria.code} - ${criteria.name}</option>`
				);
			});
		},
		error: function () {
			Swal.fire({
				icon: "error",
				title: "Gagal Memuat Kriteria",
				text: "Tidak dapat mengambil data kriteria dari server.",
				confirmButtonColor: "#dc3545",
			});
		},
	});
}

// Add Subcriteria
$("#formTambahSubkriteria").on("submit", function (e) {
	e.preventDefault();

	const code = $("#subcriteria-code").val().trim();
	const description = $("#subcriteria-description").val().trim();
	const type = $("#subcriteria-type").val();
	const targetInput = $("#subcriteria-target").val().trim();
	const criteriaId = $("#criteriaId").val();

	const target = parseFloat(targetInput);

	// Validasi angka: harus bilangan bulat 1-5
	if (isNaN(target) || target < 1 || target > 5 || !Number.isInteger(target)) {
		Swal.fire({
			icon: "warning",
			title: "<h4 class='fw-bold text-warning'>Validasi Gagal!</h4>",
			html: `<div class='mt-2'>Target harus berupa bilangan bulat antara 1 sampai 5.</div>`,
			confirmButtonText: "Periksa Lagi",
			confirmButtonColor: "#f59e0b",
			background: "#fff8e6",
		});
		return;
	}

	const formData = {
		code,
		description,
		type,
		target,
		criteriaId,
	};

	$.ajax({
		url: "/api/subcriteria",
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(formData),
		beforeSend: initializeCSRFToken(),
		success: function () {
			Swal.fire({
				icon: "success",
				title: "<h4 class='fw-bold text-success'>Berhasil!</h4>",
				html: "<div class='mt-2'>Subkriteria berhasil ditambahkan ke dalam sistem.</div>",
				showConfirmButton: false,
				timer: 2000,
				timerProgressBar: true,
				position: "center",
				background: "#e9fbe6",
			});
			$("#tabel-subkriteria").DataTable().ajax.reload();
			$("#modalTambahSubkriteria").modal("hide");
			$("#formTambahSubkriteria")[0].reset();
		},
		error: function (xhr) {
			const msg =
				xhr.responseJSON?.message ||
				"Terjadi kesalahan saat menambahkan subkriteria.";
			let htmlMessage = "";

			if (msg.includes("code") && msg.includes("already exists")) {
				htmlMessage =
					"Kode subkriteria yang Anda masukkan sudah digunakan. Gunakan kode unik lainnya.";
			} else if (msg.includes("criteria")) {
				htmlMessage = "Pastikan Anda memilih kriteria yang valid.";
			} else {
				htmlMessage = msg;
			}

			Swal.fire({
				icon: "error",
				title: "<h4 class='fw-bold text-danger'>Gagal Menambahkan!</h4>",
				html: `<div class='mt-2'>${htmlMessage}</div>`,
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
});

// Export to PDF
$("#printPDFSubcriteria").on("click", () => {
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

	fetch("/api/subcriteria/export")
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

// Get Subcriteria By ID
function findSubcriteriaById(id) {
	$.ajax({
		url: `/api/subcriteria/${id}`,
		type: "GET",
		contentType: "application/json",
		success: function (response) {
			$("#detail-id-subcriteria").text(response.id);
			$("#detail-code-subcriteria").text(response.code);
			$("#detail-description-subcriteria").text(response.description);
			$("#detail-type-subcriteria").text(response.type);
			$("#detail-target-subcriteria").text(response.target);
			$("#detail-criteria-name").text(response.criteriaName);
			$("#detail-subcriteria").modal("show");
		},
		error: function (xhr, status, error) {
			console.log(error);
			Swal.fire({
				icon: "error",
				title:
					"<h4 class='fw-bold text-danger'>Gagal Menampilkan Data Subkriteria!</h4>",
				html: "<div class='mt-2'>Terjadi kesalahan saat menampilkan data subkriteria. Silakan coba lagi.</div>",
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
}

function loadCriteriaOptionsForUpdate(selectedId) {
	$.ajax({
		url: "/api/criteria",
		type: "GET",
		success: function (data) {
			const select = $("#update-criteriaId");
			select.empty();
			select.append(`<option value="">-- Pilih Kriteria --</option>`);

			data.forEach((criteria) => {
				const isSelected = criteria.id == selectedId ? "selected" : "";
				select.append(
					`<option value="${criteria.id}" ${isSelected}>${criteria.code} - ${criteria.name}</option>`
				);
			});
		},
		error: function () {
			Swal.fire({
				icon: "error",
				title: "Gagal Memuat Kriteria",
				text: "Tidak dapat mengambil data kriteria dari server.",
				confirmButtonColor: "#dc3545",
			});
		},
	});
}

function beforeUpdateSubcriteria(id) {
	// Ambil data subkriteria berdasarkan ID
	$.ajax({
		url: `/api/subcriteria/${id}`,
		type: "GET",
		success: function (data) {
			// Set data ke dalam form
			$("#update-id-subcriteria").val(data.id);
			$("#update-code-subcriteria").val(data.code);
			$("#update-description-subcriteria").val(data.description);
			$("#update-type-subcriteria").val(data.type);
			$("#update-target-subcriteria").val(data.target);

			// Load ulang options kriteria
			loadCriteriaOptionsForUpdate(data.criteriaId);

			// Buka modal update
			$("#update-subcriteria").modal("show");
		},
		error: function () {
			Swal.fire({
				icon: "error",
				title: "<h4 class='fw-bold text-danger'>Gagal Memuat Data</h4>",
				html: "<div class='mt-2'>Tidak dapat mengambil data subkriteria.</div>",
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
			});
		},
	});
}

// Update Subcriteria
$("#formUpdateSubcriteria").on("submit", function (e) {
	e.preventDefault();

	const code = $("#update-code-subcriteria").val().trim();
	const description = $("#update-description-subcriteria").val().trim();
	const type = $("#update-type-subcriteria").val();
	const targetInput = $("#update-target-subcriteria").val().trim();
	const criteriaId = $("#update-criteriaId").val();
	const id = $("#update-id-subcriteria").val();

	const target = parseFloat(targetInput);

	// Validasi angka: harus bilangan bulat 1–5
	if (isNaN(target) || target < 1 || target > 5 || !Number.isInteger(target)) {
		Swal.fire({
			icon: "warning",
			title: "<h4 class='fw-bold text-warning'>Validasi Gagal!</h4>",
			html: `<div class='mt-2'>Target harus berupa bilangan bulat antara 1 sampai 5.</div>`,
			confirmButtonText: "Periksa Lagi",
			confirmButtonColor: "#f59e0b",
			background: "#fff8e6",
		});
		return;
	}

	const formData = {
		id,
		code,
		description,
		type,
		target,
		criteriaId,
	};

	$.ajax({
		url: `/api/subcriteria/${id}`,
		type: "PUT",
		contentType: "application/json",
		data: JSON.stringify(formData),
		beforeSend: initializeCSRFToken(),
		success: function () {
			Swal.fire({
				icon: "success",
				title: "<h4 class='fw-bold text-success'>Berhasil Diperbarui!</h4>",
				html: "<div class='mt-2'>Data subkriteria berhasil diubah.</div>",
				timer: 2000,
				timerProgressBar: true,
				showConfirmButton: false,
				background: "#e9fbe6",
			});
			$("#tabel-subkriteria").DataTable().ajax.reload();
			$("#update-subcriteria").modal("hide");
		},
		error: function (xhr) {
			const msg = xhr.responseJSON?.message || "Gagal mengupdate data.";
			let htmlMessage = "";

			if (msg.includes("code") && msg.includes("already exists")) {
				htmlMessage =
					"Kode subkriteria sudah digunakan. Gunakan kode lain yang unik.";
			} else if (msg.includes("criteria")) {
				htmlMessage = "Kriteria tidak valid atau belum dipilih.";
			} else {
				htmlMessage = msg;
			}

			Swal.fire({
				icon: "error",
				title: "<h4 class='fw-bold text-danger'>Gagal Update!</h4>",
				html: `<div class='mt-2'>${htmlMessage}</div>`,
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				background: "#fdeaea",
			});
		},
	});
});

// Delete Subcriteria
function deleteSubcriteria(id, code) {
	Swal.fire({
		title: `<h4 class="fw-bold">Hapus Subkriteria <span class="text-danger">${code}</span>?</h4>`,
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
				url: `/api/subcriteria/${id}`,
				dataType: "JSON",
				beforeSend: initializeCSRFToken(),
				contentType: "application/json",
				success: () => {
					$("#tabel-subkriteria").DataTable().ajax.reload();
					Swal.fire({
						icon: "success",
						title: "<h4 class='fw-bold text-success'>Berhasil Dihapus!</h4>",
						html: `<div class='mt-2'>Subkriteria <strong>${code}</strong> berhasil dihapus.</div>`,
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
						html: `<div class='mt-2'>Tidak dapat menghapus subkriteria <strong>${code}</strong>. Silakan coba lagi.</div>`,
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
