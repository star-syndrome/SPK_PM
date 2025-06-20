// Toggle show/hide password
function togglePassword(id) {
	const input = document.getElementById(id);
	const icon = document.getElementById(id + "-icon");

	if (input.type === "password") {
		input.type = "text";
		icon.classList.remove("fa-eye");
		icon.classList.add("fa-eye-slash");
	} else {
		input.type = "password";
		icon.classList.remove("fa-eye-slash");
		icon.classList.add("fa-eye");
	}
}

// Focus visual effect on input
document.querySelectorAll(".form-control").forEach((input) => {
	input.addEventListener("focus", function () {
		this.parentElement.classList.add("focused");
	});
	input.addEventListener("blur", function () {
		this.parentElement.classList.remove("focused");
	});
});

// Handle registration submit
$("#registration-button").click(function (e) {
	e.preventDefault();

	const valueUsername = $("#username").val();
	const valuePassword = $("#password").val();
	const valueConfirm = $("#confirmPassword").val();
	const btn = $(this);

	// Validasi password & confirmPassword cocok
	if (valuePassword !== valueConfirm) {
		Swal.fire({
			icon: "warning",
			title: "Password Tidak Cocok!",
			text: "Mohon pastikan password dan konfirmasi password sama.",
			showConfirmButton: true,
		});
		return;
	}

	// Show loading state
	btn.html('<i class="fas fa-spinner fa-spin"></i> Mendaftar...');
	btn.prop("disabled", true);

	// Kirim data ke backend
	$.ajax({
		type: "POST",
		contentType: "application/json",
		beforeSend: initializeCSRFToken(),
		dataType: "JSON",
		url: "http://localhost:8080/api/auth/registration",
		data: JSON.stringify({
			username: valueUsername,
			password: valuePassword,
			confirmPassword: valueConfirm,
		}),
		success: function (response) {
			Swal.fire({
				icon: "success",
				title: "Registrasi Berhasil 🎉",
				html: "Mengalihkan ke halaman login dalam <b></b> detik...",
				timer: 3000,
				timerProgressBar: true,
				didOpen: () => {
					const b = Swal.getHtmlContainer().querySelector("b");
					let timerInterval = setInterval(() => {
						b.textContent = Math.ceil(Swal.getTimerLeft() / 1000);
					}, 100);
				},
				willClose: () => {
					window.location.href = "/auth/login";
				},
			});
			$("#username").val("");
			$("#password").val("");
			$("#confirmPassword").val("");
		},
		error: function (error) {
			console.log(error);
			Swal.fire({
				position: "center",
				icon: "error",
				title: "Gagal Registrasi!",
				text: error.responseJSON?.message || "Terjadi kesalahan.",
				showConfirmButton: true,
			});
		},
		complete: function () {
			// Reset tombol
			btn.html("Daftar");
			btn.prop("disabled", false);
		},
	});
});
