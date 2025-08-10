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

$(document).ready(function () {
	// Input focus effect
	document.querySelectorAll(".form-control").forEach((input) => {
		input.addEventListener("focus", function () {
			this.parentElement.classList.add("focused");
		});
		input.addEventListener("blur", function () {
			this.parentElement.classList.remove("focused");
		});
	});

	// Submit logic
	$("#forgot-password-form").submit(function (e) {
		e.preventDefault();

		const username = $("#username").val();
		const newPassword = $("#newPassword").val();
		const repeatNewPassword = $("#repeatNewPassword").val();
		const btn = $("#reset-password-btn");

		if (newPassword !== repeatNewPassword) {
			Swal.fire({
				icon: "warning",
				title: "Kata Sandi Tidak Cocok!",
				text: "Pastikan kata sandi baru harus sama!",
			});
			return;
		}

		btn.html('<i class="fas fa-spinner fa-spin"></i> Resetting...');
		btn.prop("disabled", true);

		$.ajax({
			url: "http://localhost:8080/api/auth/forgot-password",
			type: "POST",
			beforeSend: initializeCSRFToken(),
			contentType: "application/json",
			dataType: "JSON",
			data: JSON.stringify({
				username: username,
				newPassword: newPassword,
				repeatNewPassword: repeatNewPassword,
			}),
			success: function (res) {
				Swal.fire({
					icon: "success",
					title: "Ubah Kata Sandi Berhasil 🎉",
					html: "Mengalihkan ke halaman masuk dalam <b></b> detik...",
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
				$("#forgot-password-form")[0].reset();
			},
			error: function (err) {
				console.log(err);
				Swal.fire({
					icon: "error",
					title: "Gagal!",
					text: "Tidak dapat mengubah kata sandi. Cek kembali nama pengguna Anda!",
				});
			},
			complete: function () {
				btn.html("Reset Password");
				btn.prop("disabled", false);
			},
		});
	});
});
