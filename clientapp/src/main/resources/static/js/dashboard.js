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

	loadCriteriaOptions();
});

// Submit Tambah Kriteria dari Quick Action Dashboard
$("#formTambahKriteria").on("submit", function (e) {
	e.preventDefault();

	const newWeight = parseFloat($("#criteria-weight").val());

	if (isNaN(newWeight)) {
		Swal.fire({
			icon: "error",
			title: "<h4 class='fw-bold text-danger'>Bobot Tidak Valid!</h4>",
			html: "<div class='mt-2'>Masukkan bobot dalam angka desimal antara <strong>0</strong> sampai <strong>1.00</strong>.</div>",
			confirmButtonText: "Perbaiki",
			confirmButtonColor: "#dc3545",
			background: "#fdeaea",
		});
		return;
	}

	// Ambil semua kriteria dulu via AJAX
	$.get("/api/criteria", function (data) {
		let totalWeight = newWeight;

		data.forEach((kriteria) => {
			const weight = parseFloat(kriteria.weight);
			if (!isNaN(weight)) {
				totalWeight += weight;
			}
		});

		// Jika melebihi 1.00, tampilkan warning
		if (totalWeight > 1) {
			Swal.fire({
				icon: "warning",
				title: "<h4 class='fw-bold text-warning'>Validasi Gagal!</h4>",
				html: `<div class='mt-2'>Total bobot semua kriteria tidak boleh lebih dari <strong>1.00</strong>.<br/>Saat ini: <strong>${totalWeight.toFixed(
					2
				)}</strong></div>`,
				confirmButtonText: "Kembali",
				confirmButtonColor: "#f59e0b",
				background: "#fff8e6",
			});
			return;
		}

		// Lolos validasi → Kirim request POST
		submitTambahKriteria();
	});
});

// Fungsi submit data ke server
function submitTambahKriteria() {
	const formData = {
		code: $("#criteria-code").val(),
		name: $("#criteria-name").val(),
		weight: $("#criteria-weight").val(),
	};

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
			$("#modalTambahKriteria").modal("hide");
			$("#formTambahKriteria")[0].reset();
		},
		error: function (xhr) {
			let message =
				xhr.responseJSON?.message ||
				"Terjadi kesalahan saat menambahkan kriteria.";
			let htmlContent;

			if (
				message.includes("code") &&
				message.toLowerCase().includes("already")
			) {
				htmlContent = `<div class="mt-2">Kode kriteria yang dimasukkan sudah digunakan. Gunakan kode lain yang unik.</div>`;
			} else {
				htmlContent = `<div class="mt-2">${message}</div>`;
			}

			Swal.fire({
				icon: "error",
				title: "<h4 class='fw-bold text-danger'>Gagal Menambahkan!</h4>",
				html: htmlContent,
				confirmButtonText: "Oke",
				confirmButtonColor: "#dc3545",
				position: "center",
				background: "#fdeaea",
			});
		},
	});
}

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
$("#addSubcriteriaForm").on("submit", function (e) {
	e.preventDefault();

	const formData = {
		code: $("#subcriteria-code").val().trim(),
		description: $("#subcriteria-description").val().trim(),
		type: $("#subcriteria-type").val(),
		target: parseFloat($("#subcriteria-target").val()),
		criteriaId: $("#criteriaId").val(),
	};

	// Validasi nilai numerik
	if (isNaN(formData.target) || formData.target < 0) {
		Swal.fire({
			icon: "warning",
			title: "<h4 class='fw-bold text-warning'>Validasi Gagal!</h4>",
			html: `<div class='mt-2'>Target harus berupa angka positif yang valid.</div>`,
			confirmButtonText: "Periksa Lagi",
			confirmButtonColor: "#f59e0b",
			background: "#fff8e6",
		});
		return;
	}

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
			$("#addSubcriteriaModal").modal("hide");
			$("#addSubcriteriaForm")[0].reset();
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

// Add Candidate
$("#addCandidateForm").on("submit", function (e) {
	e.preventDefault();

	let nama = $("#nama-kandidat").val().trim();
	let tanggalLahir = $("#tanggal-lahir").val().trim();
	let jenisKelamin = $("#jenis-kelamin").val();
	let telepon = $("#nomor-telepon").val().trim();
	let alamat = $("#alamat").val().trim();

	// Regex format tanggal dd-MM-yyyy
	let regexTanggal = /^\d{2}-\d{2}-\d{4}$/;
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

	// Validasi tanggal lahir
	if (!regexTanggal.test(tanggalLahir)) {
		Swal.fire({
			icon: "error",
			title: "Format Tanggal Salah!",
			text: "Tanggal lahir harus dalam format dd-MM-yyyy. Contoh: 09-12-2002",
			confirmButtonColor: "#dc3545",
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
				html: "<div class='mt-2'>Kandidat berhasil ditambahkan ke dalam sistem.</div>",
				showConfirmButton: false,
				timer: 2000,
				timerProgressBar: true,
				position: "center",
				background: "#e9fbe6",
			});
			$("#addCandidateModal").modal("hide");
			$("#addCandidateForm")[0].reset();
		},
		error: function (xhr) {
			const msg =
				xhr.responseJSON?.message ||
				"Terjadi kesalahan saat menambahkan kandidat.";
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
