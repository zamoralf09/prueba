// Main JavaScript file for the Security System

document.addEventListener("DOMContentLoaded", () => {
  // Initialize tooltips and other UI components
  initializeUI()

  // Handle form validations
  initializeFormValidation()

  // Handle AJAX requests
  initializeAjaxHandlers()
})

function initializeUI() {
  // Add loading states to buttons
  const buttons = document.querySelectorAll(".btn")
  buttons.forEach((button) => {
    button.addEventListener("click", function () {
      if (this.type === "submit") {
        this.innerHTML = '<span class="spinner"></span> Procesando...'
        this.disabled = true
      }
    })
  })

  // Auto-hide alerts after 5 seconds
  const alerts = document.querySelectorAll(".alert")
  alerts.forEach((alert) => {
    setTimeout(() => {
      alert.style.opacity = "0"
      setTimeout(() => alert.remove(), 300)
    }, 5000)
  })
}

function initializeFormValidation() {
  const forms = document.querySelectorAll("form")
  forms.forEach((form) => {
    form.addEventListener("submit", function (e) {
      if (!validateForm(this)) {
        e.preventDefault()
        return false
      }
    })
  })
}

function validateForm(form) {
  let isValid = true
  const requiredFields = form.querySelectorAll("[required]")

  requiredFields.forEach((field) => {
    if (!field.value.trim()) {
      showFieldError(field, "Este campo es requerido")
      isValid = false
    } else {
      clearFieldError(field)
    }
  })

  // Validate email fields
  const emailFields = form.querySelectorAll('input[type="email"]')
  emailFields.forEach((field) => {
    if (field.value && !isValidEmail(field.value)) {
      showFieldError(field, "Ingrese un email válido")
      isValid = false
    }
  })

  // Validate password fields
  const passwordFields = form.querySelectorAll('input[type="password"]')
  passwordFields.forEach((field) => {
    if (field.value && !isValidPassword(field.value)) {
      showFieldError(field, "La contraseña debe cumplir con los requisitos de seguridad")
      isValid = false
    }
  })

  return isValid
}

function showFieldError(field, message) {
  clearFieldError(field)
  field.classList.add("is-invalid")

  const errorDiv = document.createElement("div")
  errorDiv.className = "invalid-feedback"
  errorDiv.textContent = message
  field.parentNode.appendChild(errorDiv)
}

function clearFieldError(field) {
  field.classList.remove("is-invalid")
  const errorDiv = field.parentNode.querySelector(".invalid-feedback")
  if (errorDiv) {
    errorDiv.remove()
  }
}

function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

function isValidPassword(password) {
  // Basic password validation - can be enhanced based on company policies
  return password.length >= 8
}

function initializeAjaxHandlers() {
  // Handle AJAX form submissions
  const ajaxForms = document.querySelectorAll(".ajax-form")
  ajaxForms.forEach((form) => {
    form.addEventListener("submit", function (e) {
      e.preventDefault()
      submitFormAjax(this)
    })
  })
}

function submitFormAjax(form) {
  const formData = new FormData(form)
  const url = form.action || window.location.href

  fetch(url, {
    method: "POST",
    body: formData,
    headers: {
      "X-Requested-With": "XMLHttpRequest",
    },
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.success) {
        showAlert("success", data.message || "Operación exitosa")
        if (data.redirect) {
          setTimeout(() => (window.location.href = data.redirect), 1500)
        }
      } else {
        showAlert("danger", data.message || "Error en la operación")
      }
    })
    .catch((error) => {
      console.error("Error:", error)
      showAlert("danger", "Error de conexión")
    })
}

function showAlert(type, message) {
  const alertDiv = document.createElement("div")
  alertDiv.className = `alert alert-${type}`
  alertDiv.textContent = message

  const container = document.querySelector(".container")
  container.insertBefore(alertDiv, container.firstChild)

  // Auto-hide after 5 seconds
  setTimeout(() => {
    alertDiv.style.opacity = "0"
    setTimeout(() => alertDiv.remove(), 300)
  }, 5000)
}

// Utility functions
function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleDateString("es-ES")
}

function formatDateTime(dateString) {
  const date = new Date(dateString)
  return date.toLocaleString("es-ES")
}

// Export functions for use in other scripts
window.SecuritySystem = {
  showAlert,
  validateForm,
  formatDate,
  formatDateTime,
}
