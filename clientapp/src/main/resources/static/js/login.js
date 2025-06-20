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

// Add smooth focus effects
document.querySelectorAll(".form-control").forEach((input) => {
	input.addEventListener("focus", function () {
		this.parentElement.classList.add("focused");
	});

	input.addEventListener("blur", function () {
		this.parentElement.classList.remove("focused");
	});
});

// Add loading state to button
document.querySelector("form").addEventListener("submit", function () {
	const btn = document.querySelector(".login-btn");
	btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sedang Masuk...';
	btn.disabled = true;
});
