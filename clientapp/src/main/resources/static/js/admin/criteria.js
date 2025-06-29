$(document).ready(() => {
	$("#tabel-kriteria").DataTable({
		ajax: {
			url: "/api/criteria",
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
			{ data: "name" },
			{ data: "weight" },
			{
				data: null,
				render: (data) => {
					return /*html*/ `
                    <div class="d-flex m-auto gap-4 justify-content-center">
						<button
                            type="button"
                            class="btn btn-primary btn-sm"
                            data-toggle="modal"
                            data-target="#details"
                            title="Details ${data.name}"
                            onclick="findCriteriaById(${data.id})">
                            <span class="material-symbols-rounded"> info </span>
                        </button> 
                        <button
                            type="button"
                            class="btn btn-warning d-flex align-items-center"
                            data-bs-toggle="modal"
                            data-bs-target="#update"
                            title="Update ${data.name}"
                            onclick="beforeUpdateCriteria(${data.id})">
                            <span class="material-symbols-rounded"> sync </span>
                        </button>
                        <button
                            type="button"
                            class="btn btn-danger d-flex align-items-center"
                            title="Delete ${data.name}"
                            onclick="deleteCriteria(${data.id}, \`${data.name}\`)">
                            <span class="material-symbols-rounded"> delete </span>
                        </button>   
                    </div>`;
				},
			},
		],
	});

	$("#tabel-kriteria").on("draw.dt", function () {
		$("#tabel-kriteria")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});
});

// Add Criteria
$("#addCriteriaForm").on("submit", function (event) {
	event.preventDefault();

	const newWeight = parseFloat($("#weight").val());

	// Validasi angka
	if (isNaN(newWeight)) {
		Swal.fire("Error", "Nilai bobot tidak valid!", "error");
		return;
	}

	// Validasi range bobot
	if (newWeight < 0 || newWeight > 1) {
		Swal.fire({
			icon: "warning",
			title: "<h4 class='fw-bold text-warning'>Bobot Tidak Valid!</h4>",
			html: `<div class='mt-2'>Bobot harus berada dalam rentang <strong>0.00</strong> sampai <strong>1.00</strong>.</div>`,
			confirmButtonText: "Kembali",
			confirmButtonColor: "#f59e0b",
			background: "#fff8e6",
		});
		return;
	}

	let totalWeight = newWeight;

	// Tambahkan semua bobot yang sudah ada di DataTable
	$("#tabel-kriteria")
		.DataTable()
		.rows()
		.every(function () {
			const rowData = this.data(); // Akses objek data kriteria
			const weight = parseFloat(rowData.weight);
			if (!isNaN(weight)) totalWeight += weight;
		});

	// Validasi total bobot tidak melebihi 1.00
	if (totalWeight > 1) {
		Swal.fire({
			icon: "warning",
			title: "<h4 class='fw-bold text-warning'>Validasi Gagal!</h4>",
			html: `<div class='mt-2'>Total bobot kriteria tidak boleh lebih dari <strong>1.00</strong>.<br/>Saat ini: <strong>${totalWeight.toFixed(
				2
			)}</strong></div>`,
			confirmButtonText: "Kembali",
			confirmButtonColor: "#f59e0b",
			background: "#fff8e6",
		});
		return;
	}

	// Siapkan form data
	const formData = {
		code: $("#code").val(),
		name: $("#name").val(),
		weight: newWeight,
	};

	// Kirim ke back-end
	$.ajax({
		url: "/api/criteria",
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(formData),
		beforeSend: initializeCSRFToken(),
		success: function () {
			Swal.fire({
				icon: "success",
				title: "<h4 class='fw-bold text-success'>Berhasil!</h4>",
				html: "<div class='mt-2'>Kriteria berhasil ditambahkan ke dalam sistem.</div>",
				showConfirmButton: false,
				timer: 2000,
				timerProgressBar: true,
				position: "center",
				background: "#e9fbe6",
			});
			$("#tabel-kriteria").DataTable().ajax.reload();
			$("#create").modal("hide");
			$("#addCriteriaForm")[0].reset();
		},
		error: function (xhr, status, error) {
			let rawMessage = "";

			// Coba parsing JSON kalau bisa
			try {
				const json = JSON.parse(xhr.responseText);
				rawMessage = json.message || xhr.responseText;
			} catch (e) {
				rawMessage = xhr.responseText;
			}

			let htmlMessage;
			if (rawMessage.includes("Criteria code already exists")) {
				htmlMessage =
					"Kode kriteria yang dimasukkan sudah ada. Silakan gunakan kode lain.";
			} else {
				htmlMessage =
					"Terjadi kesalahan saat menambahkan kriteria. Silakan coba lagi.";
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
$("#printPDFCriteria").on("click", () => {
	window.open("/api/criteria/export", "_blank");
});

// Get Criteria By ID
function findCriteriaById(id) {
	$.ajax({
		url: `/api/criteria/${id}`,
		type: "GET",
		contentType: "application/json",
		success: function (response) {
			$("#detail-id-criteria").text(response.id);
			$("#detail-code-criteria").text(response.code);
			$("#detail-name-criteria").text(response.name);
			$("#detail-weight-criteria").text(response.weight);
			$("#detail").modal("show");
		},
		error: function (xhr, status, error) {
			console.log(error);
			Swal.fire({
				icon: "error",
				title:
					"<h4 class='fw-bold text-danger'>Gagal Menampilkan Data Kriteria!</h4>",
				html: "<div class='mt-2'>Terjadi kesalahan saat menampilkan data kriteria. Silakan coba lagi.</div>",
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
}

function beforeUpdateCriteria(id) {
	$.ajax({
		url: `/api/criteria/${id}`,
		type: "GET",
		contentType: "application/json",
		success: function (response) {
			$("#update-id-criteria").val(response.id);
			$("#update-code-criteria").val(response.code);
			$("#update-name-criteria").val(response.name);
			$("#update-weight-criteria").val(response.weight);
			$("#update").modal("show");
		},
		error: function (xhr, status, error) {
			console.log(error);
			Swal.fire({
				icon: "error",
				title:
					"<h4 class='fw-bold text-danger'>Gagal Menampilkan Data Kriteria!</h4>",
				html: "<div class='mt-2'>Terjadi kesalahan saat menampilkan data kriteria. Silakan coba lagi.</div>",
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
}

// Update Criteria
$("#formUpdateCriteria").on("submit", function (event) {
	event.preventDefault();

	let id = $("#update-id-criteria").val();
	let code = $("#update-code-criteria").val();
	let name = $("#update-name-criteria").val();
	let weight = parseFloat($("#update-weight-criteria").val());

	let totalWeight = 0;

	$("#tabel-kriteria")
		.DataTable()
		.rows()
		.every(function () {
			let rowData = this.data(); // rowData = { id, code, name, weight }
			if (rowData.id != id) {
				let currentWeight = parseFloat(rowData.weight);
				if (!isNaN(currentWeight)) totalWeight += currentWeight;
			}
		});

	totalWeight += weight;

	if (totalWeight > 1) {
		Swal.fire({
			icon: "warning",
			title: "<h4 class='fw-bold text-warning'>Validasi Gagal!</h4>",
			html: `<div class='mt-2'>Total bobot kriteria tidak boleh lebih dari <strong>1.00</strong>.<br/>Saat ini: <strong>${totalWeight.toFixed(
				2
			)}</strong></div>`,
			confirmButtonText: "Kembali",
			confirmButtonColor: "#f59e0b",
			background: "#fff8e6",
			position: "center",
		});
		return;
	}

	// Submit jika lolos validasi
	$.ajax({
		url: `/api/criteria/${id}`,
		type: "PUT",
		contentType: "application/json",
		beforeSend: initializeCSRFToken(),
		dataType: "JSON",
		data: JSON.stringify({
			code: code,
			name: name,
			weight: weight,
		}),
		success: function () {
			Swal.fire({
				icon: "success",
				title: "<h4 class='fw-bold text-success'>Berhasil Diperbarui!</h4>",
				html: "<div class='mt-2'>Kriteria berhasil diperbarui ke dalam sistem.</div>",
				showConfirmButton: false,
				timer: 2000,
				timerProgressBar: true,
				position: "center",
				background: "#e9fbe6",
			});
			$("#update").modal("hide");
			$("#tabel-kriteria").DataTable().ajax.reload();
		},
		error: function (xhr, status, error) {
			let rawMessage = "";

			// Coba parsing JSON kalau bisa
			try {
				const json = JSON.parse(xhr.responseText);
				rawMessage = json.message || xhr.responseText;
			} catch (e) {
				rawMessage = xhr.responseText;
			}

			let htmlMessage;
			if (rawMessage.includes("Criteria code already exists")) {
				htmlMessage =
					"Kode kriteria yang dimasukkan sudah ada. Silakan gunakan kode lain.";
			} else {
				htmlMessage =
					"Terjadi kesalahan saat memperbarui kriteria. Silakan coba lagi.";
			}

			Swal.fire({
				icon: "error",
				title: "<h4 class='fw-bold text-danger'>Gagal Memperbarui!</h4>",
				html: `<div class='mt-2'>${htmlMessage}</div>`,
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
});

// Delete Criteria
function deleteCriteria(id, name) {
	Swal.fire({
		title: `<h4 class="fw-bold">Hapus Kriteria <span class="text-danger">${name}</span>?</h4>`,
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
				url: `/api/criteria/${id}`,
				dataType: "JSON",
				beforeSend: initializeCSRFToken(),
				contentType: "application/json",
				success: () => {
					$("#tabel-kriteria").DataTable().ajax.reload();
					Swal.fire({
						icon: "success",
						title: "<h4 class='fw-bold text-success'>Berhasil Dihapus!</h4>",
						html: `<div class='mt-2'>Kriteria <strong>${name}</strong> berhasil dihapus.</div>`,
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
						html: `<div class='mt-2'>Tidak dapat menghapus kriteria <strong>${name}</strong>. Silakan coba lagi.</div>`,
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
