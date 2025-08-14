<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.User" %>

<%-- Security Check --%>
<%
    if (session.getAttribute("loggedInUser") == null) {
        session.setAttribute("errorMsg", "Please login to continue.");
        response.sendRedirect("login.jsp");
        return; 
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>AI Resume Tailor - AI Resume Maker</title>
    <%@include file="all_components/head.jsp" %>
    <style>
        .form-control { min-height: 48px; }
        textarea.form-control { min-height: 250px; } /* Adjusted height */
        .char-counter {
            font-size: 0.8rem;
            color: var(--text-muted);
            text-align: right;
        }
        .output-panel {
            background: rgba(0,0,0,0.3);
            border-radius: 8px;
            padding: 1.5rem;
            white-space: pre-wrap; /* Preserves formatting of the LaTeX code */
            font-family: monospace;
            font-size: 0.85rem;
            overflow-y: auto; /* Add scroll for long code */
        }
    </style>
</head>
<body>
    <div class="background-container">
        <div class="aurora aurora-1"></div>
        <div class="aurora aurora-2"></div>
    </div>

    <main>
        <%@include file="all_components/navbar.jsp" %>

        <div class="container py-5">
            <div class="row g-4">
                
                <div class="col-lg-6">
                    <div class="content-card h-100">
                        <h3 class="card-title text-center mb-2">AI-Powered Resume Tailoring</h3>
                        <p class="text-center text-muted mb-4">Provide your resume and a job description. Our AI will generate a new, optimized resume in LaTeX format.</p>
                        
                        <form action="getLatexCode" method="post" id="resume-form">
                            <div class="mb-3">
                                <label for="resumeContent" class="form-label fw-bold">1. Your Current Resume Text:</label>
                                <textarea class="form-control" id="resumeContent" name="resumeContent" rows="10" placeholder="Paste your entire resume content here..."></textarea>
                                <div id="resume-char-counter" class="char-counter mt-1">0 characters</div>
                            </div>
                            <div class="mb-3">
                                <label for="jobDescription" class="form-label fw-bold">2. Target Job Description:</label>
                                <textarea class="form-control" id="jobDescription" name="jobDescription" rows="10" placeholder="Paste the job description you are applying for..."></textarea>
                                 <div id="jd-char-counter" class="char-counter mt-1">0 characters</div>
                            </div>
                            
                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-futuristic btn-lg" id="generate-btn">
                                    <span class="btn-text"><i class="fas fa-rocket me-2"></i>Generate & Preview LaTeX</span>
                                    <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="col-lg-6">
                    <div class="content-card h-100 d-flex flex-column">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h4 class="mb-0"><i class="fab fa-latex me-2"></i>Generated LaTeX Code</h4>
                            <form action="compilePdf" method="post" target="_blank" id="pdf-download-form" class="d-none">
                                 <input type="hidden" name="latexContent" id="latex-content-hidden-input">
                                 <button type="submit" class="btn btn-success"><i class="fas fa-file-pdf me-2"></i>Download PDF</button>
                            </form>
                        </div>
                        <div id="output-panel" class="output-panel flex-grow-1 text-muted">
                            Your AI-generated LaTeX code will appear here...
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>

    <%@include file="all_components/footer.jsp" %>
    
    <script>
    document.addEventListener('DOMContentLoaded', function() {
        
        // --- Form and Button References ---
        const resumeForm = document.getElementById('resume-form');
        const generateBtn = document.getElementById('generate-btn');
        const outputPanel = document.getElementById('output-panel');
        const pdfDownloadForm = document.getElementById('pdf-download-form');
        const hiddenLatexInput = document.getElementById('latex-content-hidden-input');

        // --- Main "Generate" Button Logic ---
        resumeForm.addEventListener('submit', function(event) {
            event.preventDefault(); // Stop normal form submission

            // --- Show loading state ---
            const btnText = generateBtn.querySelector('.btn-text');
            const spinner = generateBtn.querySelector('.spinner-border');
            generateBtn.disabled = true;
            btnText.classList.add('d-none');
            spinner.classList.remove('d-none');
            pdfDownloadForm.classList.add('d-none'); // Hide download button during generation
            outputPanel.textContent = 'Generating LaTeX code... The AI is thinking, this may take a moment...';
            outputPanel.classList.remove('text-muted');

            const formData = new FormData(resumeForm);
            
            fetch('getLatexCode', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text();
            })
            .then(latexCode => {
                // Success! Show the LaTeX and the download button
                outputPanel.textContent = latexCode;
                hiddenLatexInput.value = latexCode; // Put code into the hidden input for the download form
                pdfDownloadForm.classList.remove('d-none'); // Show the download button
            })
            .catch(error => {
                console.error('Error:', error);
                outputPanel.textContent = 'An error occurred while generating the LaTeX code. Please check the server console.';
            })
            .finally(() => {
                // --- Reset button state ---
                generateBtn.disabled = false;
                btnText.classList.remove('d-none');
                spinner.classList.add('d-none');
            });
        });

        // --- Character Counters ---
        const resumeText = document.getElementById('resumeContent');
        const jdText = document.getElementById('jobDescription');
        const resumeCounter = document.getElementById('resume-char-counter');
        const jdCounter = document.getElementById('jd-char-counter');

        resumeText.addEventListener('input', () => {
            resumeCounter.textContent = `${resumeText.value.length} characters`;
        });
        jdText.addEventListener('input', () => {
            jdCounter.textContent = `${jdText.value.length} characters`;
        });
    });
    </script>
</body>
</html>