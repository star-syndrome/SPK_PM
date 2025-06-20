function togglePassword() {
	const passwordInput = document.getElementById("password");
	const passwordIcon = document.getElementById("password-icon");

	if (passwordInput.type === "password") {
		passwordInput.type = "text";
		passwordIcon.classList.remove("fa-eye");
		passwordIcon.classList.add("fa-eye-slash");
	} else {
		passwordInput.type = "password";
		passwordIcon.classList.remove("fa-eye-slash");
		passwordIcon.classList.add("fa-eye");
	}
}

// Focus effect
document.querySelectorAll(".form-control").forEach((input) => {
	input.addEventListener("focus", function () {
		this.parentElement.classList.add("focused");
	});
	input.addEventListener("blur", function () {
		this.parentElement.classList.remove("focused");
	});
});

// Validasi konfirmasi password
document.querySelector("form").addEventListener("submit", function (e) {
	const password = document.getElementById("password").value;
	const confirmPassword = document.getElementById("confirmPassword").value;
	const btn = document.querySelector(".login-btn");

	if (password !== confirmPassword) {
		e.preventDefault(); // stop form submit
		alert("Password dan konfirmasi password tidak cocok!");
		return;
	}

	btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Mendaftar...';
	btn.disabled = true;
});
