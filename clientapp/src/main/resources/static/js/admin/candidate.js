$(document).ready(() => {
	$("#tabel-kandidat").DataTable({
		ajax: {
			url: "/api/candidate",
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
			{ data: "name" },
			{ data: "dateOfBirth" },
			{ data: "gender" },
			{
				data: null,
				render: (data) => {
					return /*html*/ `
                    <div class="d-flex m-auto gap-4 justify-content-center">
						<button
                            type="button"
                            class="btn btn-primary btn-sm align-items-center"
                            data-toggle="modal"
                            data-target="#modalDetailKandidat"
                            title="Details ${data.name}"
                            onclick="findCandidateById(${data.id})">
                            <span class="material-symbols-rounded"> info </span>
                        </button> 
						<button
                            type="button"
                            class="btn btn-info btn-sm align-items-center"
                            data-toggle="modal"
                            data-target="#modalDetailCandidateScore"
                            title="Score Details ${data.name}"
                            onclick="showDetailCandidateScore(${data.id})">
                            <span class="material-symbols-rounded"> info </span>
                        </button> 
                        <button
                            type="button"
                            class="btn btn-warning d-flex align-items-center"
                            data-bs-toggle="modal"
                            data-bs-target="#modalPerbaruiKandidat"
                            title="Update ${data.name}"
                            onclick="beforeUpdateCandidate(${data.id})">
                            <span class="material-symbols-rounded"> sync </span>
                        </button>
                        <button
                            type="button"
                            class="btn btn-danger d-flex align-items-center"
                            title="Delete ${data.name}"
                            onclick="deleteCandidate(${data.id}, \`${data.name}\`)">
                            <span class="material-symbols-rounded"> delete </span>
                        </button>   
                    </div>`;
				},
			},
		],
	});

	$("#tabel-kandidat").on("draw.dt", function () {
		$("#tabel-kandidat")
			.DataTable()
			.column(0, { search: "applied", order: "applied" })
			.nodes()
			.each(function (cell, i) {
				cell.innerHTML = i + 1;
			});
	});
});

// Add Candidate
$("#formTambahKandidat").on("submit", function (e) {
	e.preventDefault();

	let nama = $("#nama-kandidat").val().trim();
	let jenisKelamin = $("#jenis-kelamin").val();
	let telepon = $("#nomor-telepon").val().trim();
	let alamat = $("#alamat").val().trim();

	let tanggalLahirRaw = $("#tanggal-lahir").val().trim(); // yyyy-MM-dd
	let [year, month, day] = tanggalLahirRaw.split("-");
	let tanggalLahir = `${day}-${month}-${year}`;

	// Regex nomor telepon hanya angka
	let regexTelepon = /^\d{10,13}$/;

	// Validasi kosong
	if (!nama || !tanggalLahir || !jenisKelamin || !telepon || !alamat) {
		Swal.fire({
			icon: "warning",
			title: "<h4 class='fw-bold text-warning'>Form Tidak Lengkap!</h4>",
			html: "<div class='mt-2'>Harap isi semua kolom dengan lengkap.</div>",
			confirmButtonText: "Oke",
			confirmButtonColor: "#f59e0b",
			background: "#fff8e6",
		});
		return;
	}

	// Validasi nomor telepon
	if (!regexTelepon.test(telepon)) {
		Swal.fire({
			icon: "error",
			title: "Nomor Telepon Tidak Valid!",
			text: "Nomor telepon harus berupa angka 10–13 digit.",
			confirmButtonColor: "#dc3545",
		});
		return;
	}

	const formData = {
		name: nama,
		dateOfBirth: tanggalLahir,
		gender: jenisKelamin,
		phone: telepon,
		address: alamat,
	};

	$.ajax({
		url: "/api/candidate",
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(formData),
		beforeSend: initializeCSRFToken(),
		success: function () {
			Swal.fire({
				icon: "success",
				title: "<h4 class='fw-bold text-success'>Berhasil!</h4>",
				html: "<div class='mt-2'>Alternatif berhasil ditambahkan ke dalam sistem.</div>",
				showConfirmButton: false,
				timer: 2000,
				timerProgressBar: true,
				position: "center",
				background: "#e9fbe6",
			});
			$("#modalTambahKandidat").modal("hide");
			$("#formTambahKandidat")[0].reset();
			$("#tabel-kandidat").DataTable().ajax.reload();
		},
		error: function (xhr) {
			const msg =
				xhr.responseJSON?.message ||
				"Terjadi kesalahan saat menambahkan alternatif.";
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

// Export to PDF
$("#printPDFCandidate").on("click", () => {
	window.open("/api/candidate/export", "_blank");
});

// Get Candidate By ID
function findCandidateById(id) {
	$.ajax({
		url: `/api/candidate/${id}`,
		type: "GET",
		contentType: "application/json",
		success: function (response) {
			$("#candidate-id").text(response.id);
			$("#candidate-name").text(response.name);
			$("#candidate-dob").text(response.dateOfBirth);
			$("#candidate-gender").text(response.gender);
			$("#candidate-phone").text(response.phone);
			$("#candidate-address").text(response.address);
			$("#modalDetailKandidat").modal("show");
		},
		error: function (xhr, status, error) {
			console.log(error);
			Swal.fire({
				icon: "error",
				title:
					"<h4 class='fw-bold text-danger'>Gagal Menampilkan Data Alternatif!</h4>",
				html: "<div class='mt-2'>Terjadi kesalahan saat menampilkan data alternatif. Silakan coba lagi.</div>",
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
}

function showDetailCandidateScore(candidateId) {
	$.ajax({
		url: `/api/candidate-score/candidate/${candidateId}`,
		method: "GET",
		success: function (data) {
			if (!data || data.length === 0) {
				Swal.fire({
					icon: "info",
					title: "Tidak Ada Skor",
					text: "Alternatif ini belum memiliki penilaian yang tersimpan.",
				});
				return;
			}

			const namaKandidat = data[0].candidateName;
			$("#detail-nama-kandidat").text(namaKandidat);

			let rows = "";
			data.forEach((item, index) => {
				rows += /*html*/ `
					<tr>
						<td>${index + 1}</td>
						<td>${item.subcriteriaCode}</td>
						<td class="text-start">${item.subcriteriaDescription}</td>
						<td>${item.score}</td>
					</tr>
				`;
			});

			$("#detail-skor-kandidat-body").html(rows);
			$("#modalDetailCandidateScore").modal("show");
		},
		error: function () {
			Swal.fire({
				icon: "error",
				title: "Gagal Memuat",
				text: "Tidak dapat mengambil data penilaian alternatif.",
			});
		},
	});
}

function beforeUpdateCandidate(id) {
	$.ajax({
		url: `/api/candidate/${id}`,
		type: "GET",
		contentType: "application/json",
		success: function (response) {
			const [day, month, year] = response.dateOfBirth.split("-");
			const dateFormatted = `${year}-${month}-${day}`; // yyyy-MM-dd

			$("#candidate-id-update").val(response.id);
			$("#candidate-name-update").val(response.name);
			$("#candidate-dob-update").val(dateFormatted);
			$("#candidate-gender-update").val(response.gender);
			$("#candidate-phone-update").val(response.phone);
			$("#candidate-address-update").val(response.address);
			$("#modalPerbaruiKandidat").modal("show");
		},
		error: function (xhr, status, error) {
			console.log(error);
			Swal.fire({
				icon: "error",
				title:
					"<h4 class='fw-bold text-danger'>Gagal Menampilkan Data Alternatif!</h4>",
				html: "<div class='mt-2'>Terjadi kesalahan saat menampilkan data alternatif. Silakan coba lagi.</div>",
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
}

// Update Candidate
$("#formPerbaruiKandidat").on("submit", function (e) {
	e.preventDefault();

	const dobRaw = $("#candidate-dob-update").val().trim(); // yyyy-MM-dd
	const [year, month, day] = dobRaw.split("-");
	const formattedDob = `${day}-${month}-${year}`;

	const formData = {
		id: $("#candidate-id-update").val(),
		name: $("#candidate-name-update").val().trim(),
		dateOfBirth: formattedDob,
		gender: $("#candidate-gender-update").val(),
		phone: $("#candidate-phone-update").val().trim(),
		address: $("#candidate-address-update").val().trim(),
	};

	// Validasi
	const regexTelepon = /^\d{10,13}$/;

	if (
		!formData.name ||
		!formData.dateOfBirth ||
		!formData.gender ||
		!formData.phone ||
		!formData.address
	) {
		Swal.fire({
			icon: "warning",
			title: "<h4 class='fw-bold text-warning'>Form Belum Lengkap!</h4>",
			html: "<div class='mt-2'>Semua kolom harus diisi.</div>",
			confirmButtonColor: "#f59e0b",
		});
		return;
	}

	if (!regexTelepon.test(formData.phone)) {
		Swal.fire({
			icon: "error",
			title: "Nomor Telepon Tidak Valid!",
			text: "Nomor telepon harus berupa angka 10–13 digit.",
			confirmButtonColor: "#dc3545",
		});
		return;
	}

	$.ajax({
		url: `/api/candidate/${formData.id}`,
		type: "PUT",
		contentType: "application/json",
		data: JSON.stringify(formData),
		beforeSend: initializeCSRFToken(),
		success: function () {
			Swal.fire({
				icon: "success",
				title: "<h4 class='fw-bold text-success'>Berhasil!</h4>",
				html: "<div class='mt-2'>Data alternatif berhasil diperbarui.</div>",
				showConfirmButton: false,
				timer: 2000,
				timerProgressBar: true,
				background: "#e9fbe6",
			});
			$("#modalPerbaruiKandidat").modal("hide");
			$("#formPerbaruiKandidat")[0].reset();
			$("#tabel-kandidat").DataTable().ajax.reload();
		},
		error: function (xhr) {
			const msg =
				xhr.responseJSON?.message ||
				"Terjadi kesalahan saat memperbarui alternatif.";
			Swal.fire({
				icon: "error",
				title: "<h4 class='fw-bold text-danger'>Gagal Memperbarui!</h4>",
				html: `<div class='mt-2'>${msg}</div>`,
				confirmButtonColor: "#dc3545",
				background: "#fdeaea",
			});
		},
	});
});

// Delete Candidate
function deleteCandidate(id, name) {
	Swal.fire({
		title: `<h4 class="fw-bold">Hapus Alternatif <span class="text-danger">${name}</span>?</h4>`,
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
				url: `/api/candidate/${id}`,
				dataType: "JSON",
				beforeSend: initializeCSRFToken(),
				contentType: "application/json",
				success: () => {
					$("#tabel-kandidat").DataTable().ajax.reload();
					Swal.fire({
						icon: "success",
						title: "<h4 class='fw-bold text-success'>Berhasil Dihapus!</h4>",
						html: `<div class='mt-2'>Alternatif <strong>${name}</strong> berhasil dihapus.</div>`,
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
						html: `<div class='mt-2'>Tidak dapat menghapus alternatif <strong>${name}</strong>. Silakan coba lagi.</div>`,
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
