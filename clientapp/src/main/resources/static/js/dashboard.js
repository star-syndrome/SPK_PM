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

// Add Criteria
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
