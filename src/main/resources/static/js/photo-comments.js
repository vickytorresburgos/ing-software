document.querySelectorAll("[data-photo-input]").forEach((input) => {
  input.addEventListener("change", () => {
    const container = input.form.querySelector("[data-comment-container]");
    container.innerHTML = "";

    Array.from(input.files).forEach((file, index) => {
      const item = document.createElement("div");
      item.className = "card card-body mb-2";
      item.innerHTML = `
        <label class="form-label" for="comentario-${index}">
          Comentario inicial para: ${file.name}
        </label>
        <textarea class="form-control" id="comentario-${index}"
          name="comentariosDeFotos" maxlength="250"
          placeholder="Opcional, máximo 250 caracteres"></textarea>`;
      container.appendChild(item);
    });
  });
});
