const $ = (selector) => document.querySelector(selector);
const enabled = $("#enabled");
const horizontal = $("#horizontal");
const vertical = $("#vertical");
const preset = $("#preset");
const preview = $("#preview");

function config() {
  return {
    enabled: enabled.value === "true",
    horizontalMultiplier: Number(horizontal.value),
    verticalMultiplier: Number(vertical.value)
  };
}

function render() {
  $("#horizontalOut").value = Number(horizontal.value).toFixed(2);
  $("#verticalOut").value = Number(vertical.value).toFixed(2);
  preview.textContent = JSON.stringify(config(), null, 2);
}

[preset, enabled, horizontal, vertical].forEach((element) => {
  element.addEventListener("input", render);
});

preset.addEventListener("change", () => {
  if (preset.value === "standard") {
    horizontal.value = "1";
    vertical.value = "1";
  } else if (preset.value === "reduced") {
    horizontal.value = "0.75";
    vertical.value = "0.75";
  }
  render();
});

$("#export").addEventListener("click", () => {
  const blob = new Blob([JSON.stringify(config(), null, 2)], {
    type: "application/json"
  });
  const url = URL.createObjectURL(blob);
  const anchor = document.createElement("a");
  anchor.href = url;
  anchor.download = "knockback.json";
  anchor.click();
  URL.revokeObjectURL(url);
});

render();
