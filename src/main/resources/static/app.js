// Only loaded on summary.html

document.addEventListener("DOMContentLoaded", () => {
    const studentSelect = document.getElementById("studentSelect");
    const summaryGrid = document.getElementById("summaryGrid");

    fetch("/api/students")
        .then(r => r.json())
        .then(students => {
            students.forEach(name => {
                const opt = document.createElement("option");
                opt.value = name;
                opt.textContent = name;
                studentSelect.appendChild(opt);
            });
            if (students.length) {
                studentSelect.value = students[0];
                loadSummary(students[0]);
            }
        });

    studentSelect.addEventListener("change", () => {
        loadSummary(studentSelect.value);
    });

    function loadSummary(name) {
        fetch("/api/summary/" + encodeURIComponent(name))
            .then(r => r.json())
            .then(student => {
                summaryGrid.innerHTML = "";
                if (!student || !student.cpacOutcomes) return;
                for (const [cpac, tokens] of Object.entries(student.cpacOutcomes)) {
                    const rowDiv = document.createElement("div");
                    rowDiv.className = "cpac-row";
                    const cpacLabel = document.createElement("span");
                    cpacLabel.textContent = cpac + ": ";
                    rowDiv.appendChild(cpacLabel);
                    tokens.forEach(t => {
                        const token = document.createElement("span");
                        token.className = "token " + (t=="y"?"y":t=="wt"?"wt":t=="n"?"n":"zero");
                        token.textContent = t.toUpperCase();
                        rowDiv.appendChild(token);
                    });
                    summaryGrid.appendChild(rowDiv);
                }
            });
    }
});

function exportPDF() {
    const element = document.getElementById("summaryGrid");
    html2pdf().from(element).save("CPAC-summary.pdf");
}
